//package cc.vihackerframework.core.flowable.configure;
//
//import org.flowable.engine.*;
//import org.flowable.spring.ProcessEngineFactoryBean;
//import org.flowable.spring.SpringProcessEngineConfiguration;
//import org.flowable.spring.boot.EngineConfigurationConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Created By Ranger on 2022/3/30
// */
//@ComponentScan(value = {
//        "org.flowable.ui.modeler.repository",
//        "org.flowable.ui.modeler.service",
//        "org.flowable.ui.common.tenant",
//        "org.flowable.ui.common.repository"})
//@Configuration
//public class ViHackerFlowableBpmnConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {
//
//    public static final String FONT="宋体";
//
//    @Bean
//    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
//        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
//        processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration);
//        return processEngineFactoryBean;
//    }
//
//    @Bean
//    public RuntimeService runtimeService(ProcessEngine processEngine) {
//        return processEngine.getRuntimeService();
//    }
//
//    @Bean
//    public HistoryService historyService(ProcessEngine processEngine) {
//        return processEngine.getHistoryService();
//    }
//
//    @Bean
//    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
//        return processEngine.getDynamicBpmnService();
//    }
//
//    @Bean
//    public TaskService taskService(ProcessEngine processEngine) {
//        return processEngine.getTaskService();
//    }
//
//    @Bean
//    public RepositoryService repositoryService(ProcessEngine processEngine) {
//        return processEngine.getRepositoryService();
//    }
//
//    @Bean
//    public IdentityService identityService(ProcessEngine processEngine) {
//        return processEngine.getIdentityService();
//    }
//
//    @Bean
//    public ManagementService managementService(ProcessEngine ProcessEngine) {
//        return ProcessEngine.getManagementService();
//    }
//
//    @Bean
//    public ProcessMigrationService processMigrationService(ProcessEngine ProcessEngine) {
//        return ProcessEngine.getProcessMigrationService();
//    }
//
//    @Bean
//    public FormService formService(ProcessEngine ProcessEngine) {
//        return ProcessEngine.getFormService();
//    }
//
//    @Override
//    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
//        engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        engineConfiguration.setDatabaseType(ProcessEngineConfiguration.DATABASE_TYPE_MYSQL);
//        engineConfiguration.setActivityFontName(FONT);
//        engineConfiguration.setAnnotationFontName(FONT);
//        engineConfiguration.setLabelFontName(FONT);
//    }
//}
