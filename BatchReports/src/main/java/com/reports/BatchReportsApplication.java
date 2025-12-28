package com.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Batch Reports.
 * Enable JMX for monitoring and management by setting below properties in vm args.
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.rmi.port=9010
-Dcom.sun.management.jmxremote.authenticate=false

 * 
 */

@SpringBootApplication
public class BatchReportsApplication { 
	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(BatchReportsApplication.class);
		Map<String, Object> manualProperties = new HashMap<>();
		manualProperties.put("spring.jmx.enabled", "true");

		app.setDefaultProperties(manualProperties); // Injected at runtime startup
		app.run(args);
	}
}
