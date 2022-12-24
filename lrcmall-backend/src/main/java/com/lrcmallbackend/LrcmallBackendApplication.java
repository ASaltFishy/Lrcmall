package com.lrcmallbackend;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
@EnableNeo4jRepositories
public class LrcmallBackendApplication {

    //kafka 消息topic
    @Bean
    public NewTopic orderRequestTopic() {
        return TopicBuilder.name("orderRequest")
                .partitions(10)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic orderReplyTopic() {
        return TopicBuilder.name("orderReply")
                .partitions(10)
                .replicas(1)
                .build();
    }

    //连接tomcat server（重定向至8443端口）
    @Bean
    public Connector connector(){
        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector){
        TomcatServletWebServerFactory tomcat=new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint=new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection=new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    public static void main(String[] args) {
        SpringApplication.run(LrcmallBackendApplication.class, args);
    }

}
