package cc.vihackerframework.flow.configure;

import org.flowable.engine.*;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created By Ranger on 2022/3/30
 */
@Configuration
public class ViHackerFlowableBpmnConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    public static final String FONT="宋体";

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engineConfiguration.setDatabaseType(ProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
        engineConfiguration.setActivityFontName(FONT);
        engineConfiguration.setAnnotationFontName(FONT);
        engineConfiguration.setLabelFontName(FONT);
    }
}
