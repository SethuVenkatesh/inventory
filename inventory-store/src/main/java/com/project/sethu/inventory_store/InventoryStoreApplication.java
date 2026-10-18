package com.project.sethu.inventory_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InventoryStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryStoreApplication.class, args);
	}

}
