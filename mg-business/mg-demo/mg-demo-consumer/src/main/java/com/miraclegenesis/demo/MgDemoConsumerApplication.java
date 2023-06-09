package com.miraclegenesis.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.miraclegenesis.demo")
public class MgDemoConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MgDemoConsumerApplication.class, args);
    }
}