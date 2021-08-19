package cc.vihackerframework.core.elasticsearch.configure;

import cc.vihackerframework.core.elasticsearch.base.ElasticsearchService;
import cc.vihackerframework.core.elasticsearch.config.ElasticsearchApplicationListener;
import cc.vihackerframework.core.elasticsearch.props.ViHackerElasticSearchProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ViHackerElasticSearchProperties.class)
public class ViHackerElasticSearchAutoConfiguration {

    private final ViHackerElasticSearchProperties elasticsearchProperties;

    @Bean
    @ConditionalOnMissingBean(RestHighLevelClient.class)
    public RestHighLevelClient createInstanceRestHighLevelClient() {
        List<HttpHost> httpHosts = new ArrayList<>();
        List<String> clusterNodes = elasticsearchProperties.getClusterNodes();
        clusterNodes.forEach(node -> {
            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.notNull(parts, "Must defined");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                httpHosts.add(new HttpHost(parts[0], Integer.parseInt(parts[1]), elasticsearchProperties.getSchema()));
            } catch (Exception e) {
                throw new IllegalStateException("Invalid ES nodes " + "property '" + node + "'", e);
            }
        });
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "123456"));
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder
                        // 线程数量
//                                .setDefaultIOReactorConfig(
//                                        IOReactorConfig.custom()
//                                                .setIoThreadCount(1)
//                                                .build())
                        // 认证设置
                        .setDefaultCredentialsProvider(credentialsProvider);
            }
        })
                // 超时时间
                .setRequestConfigCallback(
                        new RestClientBuilder.RequestConfigCallback() {
                            @Override
                            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                                return requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000);
                            }
                        });


        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    @Bean
    @ConditionalOnBean(name = "elasticsearchService")
    public ElasticsearchApplicationListener elasticsearchApplicationListener(){
        return new ElasticsearchApplicationListener();
    }

    @Bean(name = "elasticsearchService")
    @ConditionalOnBean(name = "restHighLevelClient")
    public ElasticsearchService elasticsearchService(){
        return new ElasticsearchService();
    }
}
