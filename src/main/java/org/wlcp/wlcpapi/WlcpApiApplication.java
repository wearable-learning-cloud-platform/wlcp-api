package org.wlcp.wlcpapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WlcpApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WlcpApiApplication.class, args);
	}

}
