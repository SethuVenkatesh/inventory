package com.project.sethu.employee_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class EmployeeStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeStoreApplication.class, args);
	}

}
