package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class GatewayApplication {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Add a simple re-route from: /get to: http://httpbin.org:80
                // Add a simple "Hello:World" HTTP Header
                .route(p -> p
                        .path("/bookSearch/**") // intercept calls to the /get path
                        .filters(f -> f.rewritePath("/bookSearch","")) // add header
                        .uri("lb://BOOK-SEARCH"))
                .build();
    }


    @Configuration
    public class CorsConfig {
        @Bean
        public CorsWebFilter corsWebFilter(){
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.addAllowedOrigin("http://localhost:8081");
            corsConfiguration.setAllowCredentials(true);
            source.registerCorsConfiguration("/**",corsConfiguration);
            return new CorsWebFilter(source);
        }
    }



    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}