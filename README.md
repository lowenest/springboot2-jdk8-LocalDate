[TOC]
## spring boot  与 jdk8 LocalDate LocalDateTime集成 


1. pom.xml
重点是 加入依赖`jackson-datatype-jsr310`
```xml

        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-parameter-names</artifactId>
            <version>2.9.9</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jdk8</artifactId>
            <version>2.9.9</version>
        </dependency>
<!--重点-->
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.9.9</version>
        </dependency>

```

2. 添加JDK8 LocalDATE增强序列化生成器
```java
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

```

3. 其他代码
3.1. Order.java

```java
package com.dennis.da.controller;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order implements Serializable {

    private final static long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createDate;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "createTime=" + createTime +
                ", createDate=" + createDate +
                '}';
    }
}
```

3.2. Controller
```java
package com.dennis.da.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 2020-3-27.
 */
@RequestMapping("dt")
@RestController
public class DtController {

    /**
     *
     {
     "date": "2020-03-29",
     "time": "19:30:54.468",
     "ldt": "2020-03-29 19:30:54"
     }
     * @return
     */
    @GetMapping("now")
    public Map<String, Object> nowDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", LocalDate.now());
        map.put("time", LocalTime.now());
        map.put("ldt", LocalDateTime.now());
        return map;
    }

    @GetMapping("ld")
    public LocalDate localDate(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate lc) {

        System.out.println(lc.toString());
        return lc;
    }

    @GetMapping("ldt")
    public LocalDateTime localDatetime(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime lc) {
        System.out.println(lc.toString());
        return lc;
    }

    @GetMapping("lt")
    public LocalTime locatime(
            @RequestParam
            @DateTimeFormat(pattern = "HH:mm:ss")
                    LocalTime lc) {
        System.out.println(lc.toString());
        return lc;
    }

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    @GetMapping("json-now")
    public LocalDateTime dtmLocalDt(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime lc
    ) {
        return lc;
    }

    @GetMapping("order")
    public Order koder() {
        Order or = new Order();
        or.setCreateTime(LocalDateTime.now());
        or.setCreateDate(LocalDate.now());
        return or;
    }
    @GetMapping("order3")
    public Order koder2(@RequestParam LocalDateTime createTime, @RequestParam LocalDate createDate) {
        Order or = koder();
        return or;
    }
    @PostMapping(value = "order2")
    public Order sss(@RequestBody Order order) {
        System.out.println(order);
        Order or = koder();
        return or;
    }

}
```

4 输出结果
rest-api.http
```http
GET http://localhost:8080/dt/now
Accept: */*
Cache-Control: no-cache

输出：<> 2020-03-30T102014.200.json
HTTP/1.1 200 OK
Connection: keep-alive
Transfer-Encoding: chunked
Content-Type: application/json;charset=UTF-8
Date: Mon, 30 Mar 2020 02:20:14 GMT

{
  "date": "2020-03-30",
  "time": "10:20:14",
  "ldt": "2020-03-30 10:20:14"
}


###
GET http://localhost:8080/dt/ld?lc=2019-12-22
Accept: */*
Cache-Control: no-cache

输出： <> 2020-03-30T101206.200.json
HTTP/1.1 200 OK
Connection: keep-alive
Transfer-Encoding: chunked
Content-Type: application/json;charset=UTF-8
Date: Mon, 30 Mar 2020 02:28:56 GMT

"2019-12-22"

Response code: 200 (OK); Time: 40ms; Content length: 12 bytes


###
GET http://localhost:8080/dt/ldt?lc=2019-12-22 13:44:55
Accept: */*
Cache-Control: no-cache

输出：<> 2020-03-30T101211.200.json
HTTP/1.1 200 OK
Connection: keep-alive
Transfer-Encoding: chunked
Content-Type: application/json;charset=UTF-8
Date: Mon, 30 Mar 2020 02:29:18 GMT

"2019-12-22 13:44:55"

Response code: 200 (OK); Time: 29ms; Content length: 21 bytes

###

GET http://localhost:8080/dt/lt?lc=13:44:55
Accept: */*
Cache-Control: no-cache

输出：<> 2020-03-30T101216.200.json
"13:44:55"


###

GET http://localhost:8080/dt/json-now?lc=2019-12-22 13:44:55
Accept: */*
Cache-Control: no-cache

输出：<> 2020-03-30T101220.200.json
"2019-12-22 13:44:55"


###
GET http://localhost:8080/dt/order
Accept: */*
Cache-Control: no-cache

输出：<> 2020-03-30T101224.200.json
{
  "createTime": "2020-03-30 10:14:52",
  "createDate": "2020-03-30"
}



### XXX
GET http://localhost:8080/dt/order3?createTime=2020-03-30 09:44:58&createDate=2020-03-30
Accept: */*
Cache-Control: no-cache
Content-Type: application/json

<> 2020-03-30T101452.200.json


### XXX
POST http://localhost:8080/dt/order2?createTime=2020-03-30 09:44:58&createDate=2020-03-30
Accept: */*
Cache-Control: no-cache
Content-Type: application/json

{
  "createTime": "2020-03-30 09:44:58",
  "createDate": "2020-03-30"
}

输出：<> 2020-03-30T102822.200.json
HTTP/1.1 200 OK
Connection: keep-alive
Transfer-Encoding: chunked
Content-Type: application/json;charset=UTF-8
Date: Mon, 30 Mar 2020 02:28:22 GMT

{
  "createTime": "2020-03-30 10:28:22",
  "createDate": "2020-03-30"
}


```
