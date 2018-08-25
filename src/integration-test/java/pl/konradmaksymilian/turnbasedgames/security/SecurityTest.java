package pl.konradmaksymilian.turnbasedgames.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.konradmaksymilian.turnbasedgames.user.dto.UserCreateDto;
import pl.konradmaksymilian.turnbasedgames.user.repository.LoggedUserManager;
import pl.konradmaksymilian.turnbasedgames.user.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class SecurityTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	private LoggedUserManager loggedUserManager;
	
	@Autowired
	private WebApplicationContext webContext;
	
	@Autowired
	private UserService userService;
		
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext)
				.apply(springSecurity())
				.build();
		
		userService.create(new UserCreateDto("nick5", "email5@konradmaksymilian.pl", "password5"));
	}
	@Test
	public void login_givenCorrectCredentials_logIn() throws Exception {		
		mockMvc.perform(post("/login").param("username", "nick5").param("password", "password5"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("User has successfully logged in!")));
		
		assertThat(loggedUserManager.findByNick("nick5")).isPresent();
	}
	
	@Test
	public void login_asGuest_logIn() throws Exception {
		mockMvc.perform(post("/login").param("username", "").param("password", "somePassword"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("User has successfully logged in!")));
		
		assertThat(loggedUserManager.findByNick("guest1")).isPresent();
	}
	
	@Test
	public void login_givenIncorrectNick_doesNotLogIn() throws Exception {
		mockMvc.perform(post("/login").param("username", "nick7").param("password", "password3"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Authentication failure!")));
		
		assertThat(loggedUserManager.findByNick("nick7")).isNotPresent();
	}
	
	@Test
	public void accessProtectedResource_givenNoCurrent_fails() throws Exception {
		mockMvc.perform(get("/users/1"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("The current user is not authenticated!")));
	}
	
	@Test
	@WithMockUser(roles = "GUEST")
	public void accessProtectedResource_givenInsufficientPlayerRole_fails() throws Exception {	
		mockMvc.perform(get("/users").param("nick", "nick5"))
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Access is denied")));
	}
	
	@Test
	@WithMockUser(roles = "HEAD_ADMINISTRATOR")
	public void deletesUser_givenHeadAdminRole_succeeds() throws Exception {
		int userId = userService.getByNick("nick5").getId();
		
		mockMvc.perform(delete("/users/" + userId))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void deletesUser_givenInsufficientAdminRole_fails() throws Exception {
		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Access is denied")));
	}
}
