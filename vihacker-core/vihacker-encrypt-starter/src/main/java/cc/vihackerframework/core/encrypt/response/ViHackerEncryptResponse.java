package cc.vihackerframework.core.encrypt.response;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.encrypt.annotation.Encrypt;
import cc.vihackerframework.core.encrypt.properties.ViHackerEncryptProperties;
import cc.vihackerframework.core.encrypt.util.AesUtil;
import cc.vihackerframework.core.exception.Asserts;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 接口加密
 * Created by Ranger on 2021/9/15
 */
@Slf4j
@ControllerAdvice
public class ViHackerEncryptResponse implements ResponseBodyAdvice<ViHackerApiResult> {

    private ObjectMapper om = new ObjectMapper();

    @Resource
    private ViHackerEncryptProperties encryptProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        return returnType.hasMethodAnnotation(Encrypt.class);
    }

    @Override
    public ViHackerApiResult beforeBodyWrite(ViHackerApiResult body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String key = encryptProperties.getKey();
        if(StringUtils.isBlank(key)){
            Asserts.fail("加密的 key 不能为空，请添加如下配置：\nvihacker:\n" +
                    "  encrypt:\n" +
                    "    key:");
        }
        try {
            if (body.getData() != null) {
                body.setData(AesUtil.encrypt(om.writeValueAsBytes(body.getData()).toString(), key));
                body.setEncrypt(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.error("接口返回加密失败：{}",e.getMessage());
        }
        return body;
    }
}
