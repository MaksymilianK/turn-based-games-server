package pl.konradmaksymilian.turnbasedgames.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import pl.konradmaksymilian.turnbasedgames.security.bean.GuestAuthenticationProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
		
	@Autowired
	private AuthenticationSuccessHandler http200AuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler http401AuthenticationFailureHandler;
	
	@Autowired
	private LogoutSuccessHandler http200LogoutSuccessHandler;
	
	@Autowired
	private AuthenticationEntryPoint http401AuthenticationEntryPoint;
	
	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;
	
	@Autowired
	private GuestAuthenticationProvider guestAuthenticationProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.permitAll()
				.successHandler(http200AuthenticationSuccessHandler)
				.failureHandler(http401AuthenticationFailureHandler)
				.and()
			.logout()
				.logoutSuccessHandler(http200LogoutSuccessHandler)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(http401AuthenticationEntryPoint)
				.and()
			.authenticationProvider(guestAuthenticationProvider)
			.authenticationProvider(daoAuthenticationProvider)
			.requestCache().disable();
	}
}
