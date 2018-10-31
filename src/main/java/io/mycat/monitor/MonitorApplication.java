package io.mycat.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author maxiaoguang
 */
@SpringBootApplication
public class MonitorApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MonitorApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}

}
