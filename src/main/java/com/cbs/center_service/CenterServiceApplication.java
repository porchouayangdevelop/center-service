package com.cbs.center_service;

import com.cbs.center_service.services.DataSourceManager;
import com.cbs.center_service.services.TellerService;
import com.scalar.maven.webjar.ScalarAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@SpringBootApplication
@EnableScheduling
public class CenterServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(CenterServiceApplication.class, args);



	}

}
