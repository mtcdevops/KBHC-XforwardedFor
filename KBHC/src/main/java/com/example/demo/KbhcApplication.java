package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.microsoft.applicationinsights.attach.ApplicationInsights;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class KbhcApplication {

	public static void main(String[] args) {
		ApplicationInsights.attach();
		SpringApplication.run(KbhcApplication.class, args);
	}

}
