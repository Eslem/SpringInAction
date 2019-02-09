package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tacos.persistence.IngredientRepository;
import tacos.persistence.UserRepository;

@Profile({ "!prod", "!qa" })
@Configuration
public class DevelopmentConfig {
	
	
	/*@Bean
	public CommandLineRunner dataLoader(IngredientRepository repo,
	UserRepository userRepo) {
	 return null;
	}*/
}
