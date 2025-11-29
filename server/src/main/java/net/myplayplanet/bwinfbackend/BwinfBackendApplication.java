package net.myplayplanet.bwinfbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BwinfBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BwinfBackendApplication.class, args);
	}

}
