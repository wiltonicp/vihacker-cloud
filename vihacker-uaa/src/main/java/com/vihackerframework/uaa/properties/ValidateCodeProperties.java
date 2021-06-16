package com.vihackerframework.uaa.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/25 14:50
 * @Email: wilton.icp@gmail.com
 */
@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "vihacker.captcha")
public class ValidateCodeProperties {

    /**
     * 是否启用验证码
     */
    private Boolean enable = true;

    /**
     * 验证码有效时间，单位秒
     */
    private Long time = 120L;
    /**
     * 验证码类型，可选值 png和 gif
     */
    private String type = "png";
    /**
     * 图片宽度，px
     */
    private Integer width = 130;
    /**
     * 图片高度，px
     */
    private Integer height = 48;
    /**
     * 验证码位数
     */
    private Integer length = 4;
    /**
     * 验证码值的类型
     * 1. 数字加字母
     * 2. 纯数字
     * 3. 纯字母
     */
    private Integer charType = 2;
}
