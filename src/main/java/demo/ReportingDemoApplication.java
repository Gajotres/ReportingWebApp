package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"controller","service"})
@EntityScan("model")
public class ReportingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportingDemoApplication.class, args);
	}
}
