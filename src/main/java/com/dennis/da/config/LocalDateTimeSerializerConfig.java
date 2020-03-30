package com.dennis.da.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Converter 不可优化使用Lambda表达式，否则会出现启动失败的问题
 * @author dennis*/
@Configuration
public class LocalDateTimeSerializerConfig {
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //
    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /** String --> LocalDate */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(@NotNull String source) {
                if (StringUtils.hasText(source)) {
                    return LocalDate.parse(source, DATE_FORMATTER);
                }
                return null;
            }
        };
    }

    /** String --> LocalDatetime */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(@NotNull String source) {
                if (StringUtils.hasText(source)) {
                    return LocalDateTime.parse(source, DATE_TIME_FORMATTER);
                }
                return null;
            }
        };
    }

    /** String --> LocalTime */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(@NotNull String source) {
                if (StringUtils.hasText(source)) {
                    return LocalTime.parse(source, TIME_FORMATTER);
                }
                return null;
            }
        };
    }

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
    }


    /** Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json */
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//
//        // LocalDateTime系列序列化模块，继承自jsr310，我们在这里修改了日期格式
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addSerializer(
//                LocalDateTime.class,
//                new JsonSerializer<LocalDateTime>() {
//                    @Override
//                    public void serialize(
//                            LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
//                            throws IOException {
//                        String format =
//                                value.atZone(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
//                        gen.writeString(format);
//                    }
//                });
//
//        javaTimeModule.addSerializer(
//                LocalDate.class,
//                new JsonSerializer<LocalDate>() {
//                    @Override
//                    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
//                            throws IOException {
//                        String format = value.format(DATE_FORMATTER);
//                        gen.writeString(format);
//                    }
//                });
//
//        objectMapper.registerModule(javaTimeModule);
//        return objectMapper;
//    }
}
