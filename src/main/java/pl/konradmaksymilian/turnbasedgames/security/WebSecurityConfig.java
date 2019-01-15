package pl.konradmaksymilian.turnbasedgames.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import pl.konradmaksymilian.turnbasedgames.security.bean.GuestAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		
	@Autowired
	private AuthenticationSuccessHandler http200AuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler http401AuthenticationFailureHandler;
	
	@Autowired
	private LogoutSuccessHandler http200LogoutSuccessHandler;
	
	@Autowired
	private AccessDeniedHandler http403ReturningAccessDeniedHandler;
	
	@Autowired
	private AuthenticationEntryPoint http401AuthenticationEntryPoint;
	
	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;
	
	@Autowired
	private GuestAuthenticationProvider guestAuthenticationProvider;
	
	@Autowired
	private AuthenticationEventPublisher defaultAuthenticationEventPublisher;
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
						/*.regexMatchers(HttpMethod.GET, "/users").hasRole("PLAYER")
						.antMatchers(HttpMethod.GET, "/users/administration", "/users/current", "/users/stats", 
								"/game-rooms/stats").permitAll()
						.antMatchers(HttpMethod.PATCH, "/users/{\\d+}").permitAll()
						.antMatchers("/game-rooms/{\\d+}", "/users/{\\d+}").authenticated()
						.antMatchers(HttpMethod.POST, "/game-rooms").hasRole("PLAYER")
						.antMatchers(HttpMethod.PUT, "/users/{\\d+}/role").hasRole("ADMINISTRATOR")
						.antMatchers(HttpMethod.DELETE, "users/{\\d+}").hasRole("HEAD_ADMINISTRATOR")
						.antMatchers(HttpMethod.POST,"/security-tokens", "/users").permitAll()*/
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
						.accessDeniedHandler(http403ReturningAccessDeniedHandler)
						.and()
				.sessionManagement()
						.maximumSessions(1)
						.sessionRegistry(sessionRegistry)
						.and().and()
				.cors().and()
				.csrf().disable()
				.requestCache().disable();
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) {
		auth
				.authenticationEventPublisher(defaultAuthenticationEventPublisher)
				.authenticationProvider(guestAuthenticationProvider)
				.authenticationProvider(daoAuthenticationProvider);
	}
}
