package org.kuzd4niil.testTaskForAlphaBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TestTaskForAlphaBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestTaskForAlphaBankApplication.class, args);
	}

}
