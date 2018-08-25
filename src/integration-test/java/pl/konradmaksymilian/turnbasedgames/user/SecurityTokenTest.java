package pl.konradmaksymilian.turnbasedgames.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import pl.konradmaksymilian.turnbasedgames.user.repository.SecurityTokenCacheRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Sql("/users-data.sql")
public class SecurityTokenTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private SecurityTokenCacheRepository securityTokenCacheRepository;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	public void create_givenExistingEmail_createsNewSecurityToken() throws Exception {
		mockMvc.perform(post("/security-tokens").param("email", "email5@konradmaksymilian.pl"))
				.andExpect(status().isNoContent());
		assertThat(securityTokenCacheRepository.find(2)).isPresent();
	}
	
	@Test
	public void create_givenNonExistingEmail_doesNotCreateNewSecurityToken() throws Exception {
		mockMvc.perform(post("/security-tokens").param("email", "email27@konradmaksymilian.pl"))
				.andExpect(status().isNoContent());
		for (int i = 0; i < 8; i++) {
			assertThat(securityTokenCacheRepository.find(i)).isNotPresent();
		}
	}
}
