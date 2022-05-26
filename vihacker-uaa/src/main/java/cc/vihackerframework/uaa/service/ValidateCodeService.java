package cc.vihackerframework.uaa.service;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.exception.ValidateCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/25 14:48
 * @Email: wilton.icp@gmail.com
 */
public interface ValidateCodeService {


    ViHackerApiResult getCode();

    /**
     * 获取验证码
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ValidateCodeException
     */
    void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException;

    /**
     * 获取短信验证码
     * @param mobile 手机号
     * @return
     */
    ViHackerApiResult<?> getSmsCode(String mobile);

    /**
     * 校验验证码
     *
     * @param key  　KEY
     * @param code 验证码
     * @throws ValidateCodeException 验证码异常
     */
    void check(String key, String code) throws ValidateCodeException;

}
