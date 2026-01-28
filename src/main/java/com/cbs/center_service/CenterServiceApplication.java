package com.cbs.center_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class CenterServiceApplication {


  public static void main(String[] args) {
    SpringApplication.run(CenterServiceApplication.class, args);


  }

}
