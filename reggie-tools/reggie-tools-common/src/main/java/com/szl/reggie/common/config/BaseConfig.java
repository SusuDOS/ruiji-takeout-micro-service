package com.szl.reggie.common.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.szl.reggie.base.id.CodeGenerate;
import com.szl.reggie.common.converter.EnumDeserializer;
import com.szl.reggie.common.converter.String2DateConverter;
import com.szl.reggie.common.converter.String2LocalDateConverter;
import com.szl.reggie.common.converter.String2LocalDateTimeConverter;
import com.szl.reggie.common.converter.String2LocalTimeConverter;
import com.szl.reggie.common.undertow.UndertowServerFactoryCustomizer;
import com.szl.reggie.utils.SpringUtils;

import com.szl.reggie.base.id.CodeGenerate;
import com.szl.reggie.utils.DateUtils;
import com.szl.reggie.utils.SpringUtils;
import io.undertow.Undertow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.szl.reggie.utils.DateUtils.DEFAULT_DATE_FORMAT;
import static com.szl.reggie.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.szl.reggie.utils.DateUtils.DEFAULT_TIME_FORMAT;

/**
 * ???????????????
 *
 */
public abstract class BaseConfig {

    /**
     * ??????????????????????????????????????????????????????
     * addSerializer???????????? ???Controller ?????? ????????????json???
     * Long -> string
     * BigInteger -> string
     * BigDecimal -> string
     * date -> string
     * <p>
     * addDeserializer: ???????????? ?????????????????????????????????????????????json???
     * ????????????: {"code": "xxx"} -> Enum
     *
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.simpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT);
        ObjectMapper objectMapper = builder.createXmlMapper(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .timeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .build();

        objectMapper
                .setLocale(Locale.CHINA)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //??????????????????
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //???????????????parser????????????JSON????????????????????????????????????????????????32???ASCII?????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????JSON?????????????????????????????????????????????????????????????????????????????????????????????
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                // ???????????????????????????
                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                //???????????????
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                //????????????
                .setDateFormat(new SimpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT));

        //????????????????????????????????????????????????
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance)
                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(BigDecimal.class, ToStringSerializer.instance)
                .addDeserializer(Enum.class, EnumDeserializer.INSTANCE);

        return objectMapper.registerModule(simpleModule);
    }

    /**
     * serializerByType ??????json???????????? LocalDateTime ????????????
     * deserializerByType ??????string?????????????????? LocalDateTime ????????????
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_FORMAT)))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMAT)))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMAT)))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_TIME_FORMAT)));
    }

    /**
     * ?????? @RequestParam(value = "date") Date date
     * date ???????????? ????????????
     *
     * @return
     */
    @Bean
    public Converter<String, Date> dateConvert() {
        return new String2DateConverter();
    }

    /**
     * ?????? @RequestParam(value = "time") LocalDate time
     *
     * @return
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new String2LocalDateConverter();
    }

    /**
     * ?????? @RequestParam(value = "time") LocalTime time
     *
     * @return
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new String2LocalTimeConverter();
    }

    /**
     * ?????? @RequestParam(value = "time") LocalDateTime time
     *
     * @return
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new String2LocalDateTimeConverter();
    }

    //---------------------------------------???????????????end----------------------------------------------

    /**
     * ????????????8???????????????
     *
     * @param machineCode
     * @return
     */
    @Bean("codeGenerate")
    public CodeGenerate codeGenerate(@Value("${id-generator.machine-code:1}") Long machineCode) {
        return new CodeGenerate(machineCode.intValue());
    }

    /**
     * Spring ?????????
     *
     * @param applicationContext
     * @return
     */
    @Bean
    public SpringUtils getSpringUtils(ApplicationContext applicationContext) {
        SpringUtils.setApplicationContext(applicationContext);
        return new SpringUtils();
    }


    @Bean
    @ConditionalOnClass(Undertow.class)
    public UndertowServerFactoryCustomizer getUndertowServerFactoryCustomizer() {
        return new UndertowServerFactoryCustomizer();
    }
}
