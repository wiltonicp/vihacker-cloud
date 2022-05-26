package cc.vihackerframework.core.doc.configure;

import cc.vihackerframework.core.factory.YamlPropertySourceFactory;
import cc.vihackerframework.core.doc.properties.ViHackerDocProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
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
import springfox.documentation.PathProvider;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.DefaultPathProvider;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final OpenApiExtensionResolver openApiExtensionResolver;

    public ViHackerDocAutoConfigure(ViHackerDocProperties properties, OpenApiExtensionResolver openApiExtensionResolver) {
        this.properties = properties;
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    @Bean
    public PathProvider pathProvider1() {
        return new DefaultPathProvider() {
            @Override
            public String getOperationPath(String operationPath) {
                return super.getOperationPath(operationPath);
            }
        };
    }

    /**
     * Swagger忽略的参数类型
     */
    private final Class[] ignoredParameterTypes = new Class[]{
            ServletRequest.class,
            ServletResponse.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            HttpSession.class,
            ApiIgnore.class,
            Principal.class,
            Map.class
    };

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
                .pathProvider(pathProvider1())
                .ignoredParameterTypes(ignoredParameterTypes)
                .pathMapping("/").extensions(openApiExtensionResolver.buildSettingExtensions())
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
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

    private List<ApiKey> securitySchemes(){
        List<ApiKey> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts(){
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }
}
