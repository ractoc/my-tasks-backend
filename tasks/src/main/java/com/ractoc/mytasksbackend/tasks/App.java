package com.ractoc.mytasksbackend.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class, scanBasePackages = {"com.ractoc.mytasksbackend.tasks", "com.ractoc.mytasksbackend.common"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
