package com.lrcmallbackend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
        @Bean
        public ObjectMapper ObjectMapper(){
            ObjectMapper objectMapper=new ObjectMapper();
            return objectMapper;
        }
}
