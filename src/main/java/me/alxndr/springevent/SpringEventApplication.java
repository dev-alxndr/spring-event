package me.alxndr.springevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringEventApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEventApplication.class, args);
	}

}
