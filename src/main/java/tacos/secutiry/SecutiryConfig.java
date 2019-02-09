package tacos.secutiry;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecutiryConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * auth.inMemoryAuthentication().withUser("buzz").password("infinity").
	 * authorities("ROLE_USER").and()
	 * .withUser("woody").password("bullseye").authorities("ROLE_USER"); }
	 */
	/*
	 * @SuppressWarnings("deprecation")
	 * 
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { auth .jdbcAuthentication() .dataSource(dataSource)
	 * .usersByUsernameQuery( "select username, password, enabled from Users " +
	 * "where username=?") .authoritiesByUsernameQuery(
	 * "select username, authority from UserAuthorities " + "where username=?")
	 * .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
	 * 
	 * }
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/design", "/orders").access("hasRole('USER')").antMatchers("/", "/**")
				.access("permitAll")

				.and().formLogin().loginPage("/login").usernameParameter("email")

				.and().logout().logoutSuccessUrl("/");

		http.csrf().ignoringAntMatchers("/h2-console/**")

				// Allow pages to be loaded in frames from the same origin; needed for
				// H2-Console
				.and().headers().frameOptions().sameOrigin();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	@Bean
	public PasswordEncoder encoder() {
		return new StandardPasswordEncoder("53cr3t");
	}

}
