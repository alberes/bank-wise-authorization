package io.github.alberes.bank.wise.authorization.config;

import io.github.alberes.bank.wise.authorization.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateConfig {

    @Bean
    public DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMATTER_PATTERN);
    }
}