package cc.vihackerframework.core.encrypt.response;

import cc.vihackerframework.core.encrypt.annotation.Decrypt;
import cc.vihackerframework.core.encrypt.properties.ViHackerEncryptProperties;
import cc.vihackerframework.core.encrypt.util.AesUtil;
import cc.vihackerframework.core.exception.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 解密接口
 * Created by Ranger on 2021/9/15
 */
@Slf4j
@ControllerAdvice
public class ViHackerDecryptRequest extends RequestBodyAdviceAdapter {

    @Resource
    private ViHackerEncryptProperties encryptProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if(StringUtils.isBlank(encryptProperties.getKey())){
            Asserts.fail("解密的 key 不能为空，请添加如下配置：\nvihacker:\n" +
                    "  encrypt:\n" +
                    "    key:");
        }
        byte[] body = new byte[inputMessage.getBody().available()];
        String bodyStr = new String(body);
        try {
            String decryptStr = AesUtil.decrypt(bodyStr, encryptProperties.getKey());
            byte[] decrypt = decryptStr.getBytes();
            final ByteArrayInputStream bais = new ByteArrayInputStream(decrypt);
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return bais;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            log.error("参数解密失败：{}",e.getMessage());
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
