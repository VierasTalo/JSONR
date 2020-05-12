package fi.hh.jsonresume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fi.hh.jsonresume.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class JsonresumeApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(JsonresumeApplication.class, args);
	}
}
