package com.realyoungk.sdi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SdiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdiApplication.class, args);
	}

}
