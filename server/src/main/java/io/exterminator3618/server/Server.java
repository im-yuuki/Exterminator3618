package io.exterminator3618.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
	}

}
