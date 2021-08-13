package cc.vihackerframework.doc.starter.configure;

import cc.vihackerframework.core.factory.YamlPropertySourceFactory;
import cc.vihackerframework.doc.starter.properties.ViHackerDocProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/4
 */
@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
@EnableConfigurationProperties(ViHackerDocProperties.class)
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:vihacker-doc.yml")
@ConditionalOnProperty(value = ViHackerDocProperties.PREFIX + ".enable", havingValue = "true", matchIfMissing = true)
public class ViHackerDocAutoConfigure {

    private final ViHackerDocProperties properties;

    public ViHackerDocAutoConfigure(ViHackerDocProperties properties) {
        this.properties = properties;
    }


    @Bean
    @Order(-1)
    public Docket createRestApi() {
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .select();
        if(properties.getBasePackage() == null){
            apiSelectorBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        }else{
            apiSelectorBuilder.apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()));
        }
        return apiSelectorBuilder
                .paths(PathSelectors.any())
                .build()
                .enable(properties.getEnable())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
    }

    private ApiInfo groupApiInfo() {
        String description = String.format("<div style='font-size:%spx;color:%s;'>%s</div>",
                properties.getDescriptionFontSize(), properties.getDescriptionColor(), properties.getDescription());

        Contact contact = new Contact(properties.getName(), properties.getUrl(), properties.getEmail());

        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(description)
                .termsOfServiceUrl(properties.getServiceUrl())
                .contact(contact)
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .version(properties.getVersion())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
}
