package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import tacos.persistence.IngredientRepository;
import tacos.persistence.UserRepository;

@SpringBootApplication
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

	@Bean
	//@Profile({ "dev", "qa" })
	@Profile("!prod")
	public CommandLineRunner dataLoader(IngredientRepository repo, UserRepository userRepo) {
		return null;
	}

}
