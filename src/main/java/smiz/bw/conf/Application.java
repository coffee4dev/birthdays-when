package smiz.bw.conf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import smiz.bw.c.BirthdaysController;

/**
 * Created by smiz on 01/12/16.
 */
@Configuration
@ComponentScan(basePackageClasses = {BirthdaysController.class})
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
