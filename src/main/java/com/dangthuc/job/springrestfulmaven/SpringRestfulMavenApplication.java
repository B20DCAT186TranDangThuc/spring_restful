package com.dangthuc.job.springrestfulmaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@SpringBootApplication(exclude = {
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
//})
@EnableAsync
@EnableScheduling
public class SpringRestfulMavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestfulMavenApplication.class, args);
    }

}
