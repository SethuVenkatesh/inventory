package com.project.sethu.inventory_store;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
		info = @Info(title = "Inventory Store API",
				version = "1.0",
				description = "API documentation for Inventory Store")
)
public class InventoryStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryStoreApplication.class, args);
	}

}
