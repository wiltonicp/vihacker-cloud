package cc.vihackerframework.core.datasource.configure;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 序列化和反序列化配置
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/4/22
 */
@Configuration
public class ViHackerJacksonConfigure {

    @Bean
    @Order(-2147483648)
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return (builder) -> {

            /**
             * Jackson全局转化long类型为String
             * 解决jackson序列化时传入前端Long类型缺失精度问题
             */
//            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
//            builder.serializerByType(BigDecimal.class, ToStringSerializer.instance);
//            builder.serializerByType(Long.class, ToStringSerializer.instance);

            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

            builder.failOnUnknownProperties(false);
            builder.featuresToDisable(new Object[]{SerializationFeature.WRITE_DATES_AS_TIMESTAMPS});
        };
    }


}
