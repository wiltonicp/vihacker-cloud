package cc.vihackerframework.core.elasticsearch.config;

import cc.vihackerframework.core.elasticsearch.annotation.ESDocument;
import cc.vihackerframework.core.elasticsearch.base.BaseElasticsearchDao;
import cc.vihackerframework.core.elasticsearch.base.ElasticsearchService;
import cc.vihackerframework.core.exception.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Slf4j
//@Component
public class ElasticsearchApplicationListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    @Resource
    protected ElasticsearchService elasticsearchService;

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        String[] beanNames = applicationContext.getBeanNamesForType(BaseElasticsearchDao.class);
        if (beanNames != null && beanNames.length > 0) {
            for (int i = 0; i < beanNames.length; i++) {
                String beanName = beanNames[i];
                if (beanName.contains("cc.vihackerframework.core.elasticsearch.base.BaseElasticsearchDao")) {
                    continue;
                }
                Object bean = applicationContext.getBean(beanName);
                Class<?> targetBeanClass = bean.getClass();
                Class<?> beanClass = GenericTypeResolver.resolveTypeArgument(targetBeanClass, BaseElasticsearchDao.class);
                ESDocument esDocument = AnnotationUtils.findAnnotation(beanClass, ESDocument.class);
                if (esDocument == null) {
                    Asserts.fail("ESDocument注解未指定");
                }
                String indexName = esDocument.indexName();
                if (StringUtils.isEmpty(indexName)) {
                    Asserts.fail("indexName未指定");
                }
                int shards = esDocument.shards();
                int replicas = esDocument.replicas();

                if (shards == 0 || replicas == 0) {
                    elasticsearchService.createIndexRequest(indexName);
                } else {
                    elasticsearchService.createIndexRequest(indexName, shards, replicas);
                }
                elasticsearchService.putMappingRequest(indexName, beanClass);
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}