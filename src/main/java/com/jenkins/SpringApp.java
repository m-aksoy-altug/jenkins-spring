package com.jenkins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class SpringApp {
	
private static final Logger log= LoggerFactory.getLogger(SpringApp.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class,args);
	}

}
