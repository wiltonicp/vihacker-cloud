package cc.vihackerframework.core.datasource.starter.configure;

import cc.vihackerframework.core.mybatis.handler.ViHackerMetaObjectHandler;
import cc.vihackerframework.core.mybatis.injector.ViHackerSqlInjector;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.type.EnumTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * MyBatisPlus配置
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/4
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@MapperScan("cc.vihackerframework.**.mapper.**")
public class ViHackerDataSourceAutoConfigure {

    /**
     * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
     */
    private static final Long MAX_LIMIT = 1000L;

    /**
     * sql 注入
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new ViHackerSqlInjector();
    }

    @Bean
    @Order(-2)
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件: PaginationInnerInterceptor
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
        //防止全表更新与删除插件: BlockAttackInnerInterceptor
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return interceptor;
    }

    /**
     * 自动填充数据
     */
    @Bean
    @ConditionalOnMissingBean(ViHackerMetaObjectHandler.class)
    public ViHackerMetaObjectHandler mateMetaObjectHandler() {
        return new ViHackerMetaObjectHandler();
    }

    /**
     * IEnum 枚举配置
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
            // 关闭 mybatis 默认的日志
            //configuration.setLogPrefix("log.mybatis");
        };
    }

    /**
     * 乐观锁拦截器
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

}
