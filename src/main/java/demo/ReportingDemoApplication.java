package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"controller","service"})
@EntityScan("model")
@EnableJpaRepositories("dao")
public class ReportingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportingDemoApplication.class, args);
	}
}
