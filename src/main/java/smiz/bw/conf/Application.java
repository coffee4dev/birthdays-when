package smiz.bw.conf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import smiz.bw.components.BirthdaysController;
import smiz.bw.model.Person;
import smiz.bw.repo.PersonRepository;

/**
 * Spring App configuration class
 */
@SpringBootApplication(scanBasePackageClasses = {BirthdaysController.class})
@EnableJpaRepositories(basePackageClasses = {PersonRepository.class})
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Person.class})
@EnableScheduling
@EnableAspectJAutoProxy
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
		ex.setCorePoolSize(5);
		ex.setMaxPoolSize(10);
		ex.setWaitForTasksToCompleteOnShutdown(true);
		return ex;
	}

}
