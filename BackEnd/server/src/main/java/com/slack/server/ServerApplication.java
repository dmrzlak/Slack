package com.slack.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {
	/**
	 * Base application to run the server. Springboot takes care of it for us, so we don't need much logic at all
	 * This class/method has visibility to the rest of the classes specified and creates 'beans' for the repositories,
	 * creates the controllers onto the server page, and then the controllers handle logic
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
