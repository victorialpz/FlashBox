package es.uclm.FlashBox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "es.uclm.FlashBox.business.entity")
@EnableJpaRepositories(basePackages = "es.uclm.FlashBox.business.persistence")
public class FlashBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashBoxApplication.class, args);
	}

}
