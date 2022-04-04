package cc.vihackerframework.uaa.service.impl;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.redis.starter.service.RedisService;
import cc.vihackerframework.uaa.properties.ValidateCodeProperties;
import cc.vihackerframework.uaa.service.ValidateCodeService;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码业务实现类
 * Created by Ranger on 2022/3/19
 */
@Slf4j
@Service
@AllArgsConstructor
public class ValidateCodeServiceImpl implements ValidateCodeService {

    private final RedisService redisService;

    @Override
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        String key = request.getParameter(Oauth2Constant.CAPTCHA_HEADER_KEY);
        if (StringUtils.isBlank(key)) {
            throw new ValidateCodeException("验证码key不能为空");
        }
        ValidateCodeProperties code = new ValidateCodeProperties();
        setHeader(response, code.getType());

        Captcha captcha = createCaptcha(code);
        redisService.set(Oauth2Constant.CAPTCHA_KEY + key, StringUtils.lowerCase(captcha.text()), code.getTime());
        captcha.out(response.getOutputStream());
    }

    @Override
    public ViHackerApiResult<?> getSmsCode(String mobile) {
        String code = "8888";
        redisService.set(Oauth2Constant.SMS_CODE_KEY + mobile, code, 300L);
        return ViHackerApiResult.success("发送成功");
    }

    /**
     * 校验验证码
     * @param key  　KEY
     * @param code 验证码
     * @throws ValidateCodeException
     */
    @Override
    public void check(String key, String code) throws ValidateCodeException {
        Object codeInRedis = redisService.get(Oauth2Constant.CAPTCHA_KEY + key);
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeException("请输入验证码");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(code, String.valueOf(codeInRedis))) {
            throw new ValidateCodeException("验证码不正确");
        }
        redisService.del(Oauth2Constant.CAPTCHA_KEY + key);
    }

    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha = null;
        if (StringUtils.equalsIgnoreCase(code.getType(), ViHackerConstant.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    private void setHeader(HttpServletResponse response, String type) {
        if (StringUtils.equalsIgnoreCase(type, ViHackerConstant.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
