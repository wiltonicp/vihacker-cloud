package com.vihackerframework.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于配置白名单资源路径
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/1/25
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vihacker.security.ignored")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();
}
