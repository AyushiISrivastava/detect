package com.vehicle.detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Ayushi on 21/01/19.
 */

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.vehicle")
public class SpringApplicationStarted {

	public static void main(String args[]) {
		SpringApplication.run(SpringApplicationStarted.class, args);
	}

}
