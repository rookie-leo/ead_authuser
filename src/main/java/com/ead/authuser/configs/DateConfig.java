package com.ead.authuser.configs;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateConfig {

    public static final String DATETIME_FORMTA = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMTA)));
    }

}
