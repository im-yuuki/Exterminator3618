package io.exterminator3618.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class ServerApplication extends SpringApplication {

    private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);

	public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
	}

}
