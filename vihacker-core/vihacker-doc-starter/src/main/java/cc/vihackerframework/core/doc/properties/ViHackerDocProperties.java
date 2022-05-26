package cc.vihackerframework.core.doc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文档配置
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/4
 */
@Getter
@Setter
@ConfigurationProperties(ViHackerDocProperties.PREFIX)
public class ViHackerDocProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "vihacker.doc";


    /**
     * 是否开启doc功能
     */
    private Boolean enable = Boolean.TRUE;
    /**
     * 接口扫描路径，如Controller路径
     */
    private String basePackage;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档描述
     */
    private String description;
    /**
     * 文档描述颜色
     */
    private String descriptionColor = "#42b983";
    /**
     * 文档描述字体大小
     */
    private String descriptionFontSize = "14";

    /**
     * 密码模式获取token的地址
     */
    private String passwordTokenUrl = "http://localhost:10001/vihacker-uaa/oauth/token";
    /**
     * 服务url
     */
    private String serviceUrl;
    /**
     * 联系方式：姓名
     */
    private String name;
    /**
     * 联系方式：个人网站url
     */
    private String url;
    /**
     * 联系方式：邮箱
     */
    private String email;
    /**
     * 协议
     */
    private String license;
    /**
     * 协议地址
     */
    private String licenseUrl;
    /**
     * 版本
     */
    private String version;
}
