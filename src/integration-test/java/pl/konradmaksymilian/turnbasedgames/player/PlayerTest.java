package pl.konradmaksymilian.turnbasedgames.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerCreateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerRoleUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;
import pl.konradmaksymilian.turnbasedgames.player.repository.PlayerRepository;
import pl.konradmaksymilian.turnbasedgames.player.service.SecurityTokenProvider;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Sql("/players-data.sql")
public class PlayerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webContext;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private SecurityTokenProvider securityTokenProvider;
	
	@BeforeEach
	public void setUp() {
		assertThat(webContext).isNotNull();
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	@WithMockUser(roles = "GUEST")
	public void get_givenExistingId_returnsDto() throws Exception {
		mockMvc.perform(get("/players/5").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(5)))
				.andExpect(jsonPath("$.nick", is("nick6")))
				.andExpect(jsonPath("$.role", is("PLAYER")));
	}
	
	@Test
	@WithMockUser(roles = "HEAD_ADMINISTRATOR")
	public void get_givenNonExistingId_returnsExceptionDto() throws Exception {		
		mockMvc.perform(get("/players/10").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Cannot find the player with the given id: 10")));
	}
	
	@Test
	public void get_givenNoAuthenticatedPlayer_returnsExceptionDto() throws Exception {		
		mockMvc.perform(get("/players/10").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("The current player is not authenticated!")));
	}
	
	@Test
	@WithUserDetails(value = "nick1")
	public void getCurrent_givenAuthenticatedPlayer_returnsDto() throws Exception {		
		mockMvc.perform(get("/players/current").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.nick", is("nick1")))
				.andExpect(jsonPath("$.role", is("HEAD_ADMINISTRATOR")));
	}
	
	@Test
	public void getCurrent_givenAuthenticatedGuest_returnsExceptionDto() throws Exception {		
		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(
				new Player("guest1", "", "", Role.GUEST), ""));
		
		mockMvc.perform(get("/players/current").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Cannot find the current player; the player is not logged in")));
	}
	
	@Test
	public void getCurrent_givenNoAuthenticatedPlayer_returnsExceptionDto() throws Exception {		
		mockMvc.perform(get("/players/current").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Cannot find the current player; the player is not logged in")));
	}
	
	@Test
	@WithMockUser(roles = "PLAYER")
	public void getByNick_givenExistingNick_returnsDto() throws Exception {		
		mockMvc.perform(get("/players").param("nick", "nick4").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(6)))
				.andExpect(jsonPath("$.nick", is("nick4")))
				.andExpect(jsonPath("$.role", is("HELPER")));
	}
	
	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void getByNick_givenNonExistingNick_returnsExceptionDto() throws Exception {		
		mockMvc.perform(get("/players").param("nick", "nick16").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Cannot find the player with the given nick: 'nick16'")));
	}
	
	@Test
	@WithMockUser(roles = "GUEST")
	public void getByNick_givenAuthenticatedGuest_returnsDto() throws Exception {		
		mockMvc.perform(get("/players").param("nick", "nick1").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Access is denied")));
	}
	
	@Test
	@WithMockUser(roles = "HELPER")
	public void getPage_givenLegalPageRequest_returnsDto() throws Exception {		
		var request = get("/players")
				.param("size", "3")
				.param("page", "1")
				.param("sort", "role,desc")
				.param("sort", "nick")
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.size", is(3)))
				.andExpect(jsonPath("$.page", is(1)))
				.andExpect(jsonPath("$.totalElements", is(7)))
				.andExpect(jsonPath("$.sort.role", is("DESC")))
				.andExpect(jsonPath("$.sort.nick", is("ASC")))
				.andExpect(jsonPath("$.content", hasSize(3)))
				.andExpect(jsonPath("$.content[0].id", is(6)))
				.andExpect(jsonPath("$.content[1].id", is(2)))
				.andExpect(jsonPath("$.content[2].id", is(5)));
	}
	
	@Test
	@WithMockUser(roles = "HELPER")
	public void getPage_givenTooBigPageSize_returnsExceptionDto() throws Exception {		
		var request = get("/players")
				.param("size", "101")
				.param("sort", "nick")
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("illegal player page request")));
	}
	
	@Test
	@WithMockUser(roles = "HELPER")
	public void getPage_givenSortByNonPublicData_returnsExceptionDto() throws Exception {		
		var request = get("/players")
				.param("size", "3")
				.param("page", "1")
				.param("sort", "email")
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("illegal player page request")));
	}
	
	@Test
	@WithMockUser(roles = "HELPER")
	public void getPage_givenNonExistingPage_returnsDto() throws Exception {		
		var request = get("/players")
				.param("size", "3")
				.param("page", "5")
				.param("sort", "role")
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.size", is(3)))
				.andExpect(jsonPath("$.page", is(5)))
				.andExpect(jsonPath("$.totalElements", is(7)))
				.andExpect(jsonPath("$.sort.role", is("ASC")))
				.andExpect(jsonPath("$.content", hasSize(0)));
	}
	
	@Test
	@WithMockUser(roles = "PLAYER")
	public void getPage_givenAuthenticatedPlayer_returnsExceptionDto() throws Exception {		
		var request = get("/players")
				.param("size", "3")
				.param("page", "1")
				.param("sort", "role")
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Access is denied")));
	}
	
	@Test
	public void getAdministration_givenNoAuthenticatedPlayer_returnsDto() throws Exception {		
		mockMvc.perform(get("/players/administration").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].role", not("PLAYER")))
				.andExpect(jsonPath("$[1].role", not("PLAYER")))
				.andExpect(jsonPath("$[2].role", not("PLAYER")))
				.andExpect(jsonPath("$[3].role", not("PLAYER")));
	}
	
	@Test
	public void create_givenLegalData_createsNewPlayer() throws Exception {
		var dto = new PlayerCreateDto();
		dto.setNick("nick8");
		dto.setEmail("validEmail@konradmaksymilian.pl");
		dto.setPassword("password");
		
		var request = post("/players")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(header().string("location", endsWith("/players/8")))
				.andExpect(jsonPath("$.description", is("Created a new player!")));
		var player = playerRepository.findById(8).get();
		assertThat(player.getNick()).isEqualTo("nick8");
		assertThat(player.getEmail()).isEqualTo("validEmail@konradmaksymilian.pl");
		assertThat(player.getPassword()).isEqualTo("password");
	}
	
	@Test
	public void create_givenExistingNick_returnsExceptionDto() throws Exception {
		var dto = new PlayerCreateDto();
		dto.setNick("nick5");
		dto.setEmail("validEmail@konradmaksymilian.pl");
		dto.setPassword("password");
		
		var request = post("/players")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Player with the given nick: 'nick5' already exists in data base")));
	}
	
	@Test
	public void create_givenIllegalNick_returnsExceptionDto() throws Exception {
		var dto = new PlayerCreateDto();
		dto.setNick("admin8");
		dto.setEmail("validEmail@konradmaksymilian.pl");
		dto.setPassword("password");
		
		var request = post("/players")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("nick: illegal nick")));
	}
	
	@Test
	@WithUserDetails(value = "nick2")
	public void updateRole_givenNewHelperRole_updatesRole() throws Exception {		
		var dto = new PlayerRoleUpdateDto();
		dto.setNewRole(Role.HELPER);
		
		SecurityContextHolder.getContext();
		
		var request = put("/players/3/role")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
				
		mockMvc.perform(request)
				.andExpect(status().isNoContent());
		assertThat(playerRepository.findById(3).get().getRole()).isEqualTo(Role.HELPER);
	}
	
	@Test
	@WithUserDetails(value = "nick1")
	public void updateRole_givenNewGuestRole_returnsExceptionDto() throws Exception {	
		var dto = new PlayerRoleUpdateDto();
		dto.setNewRole(Role.GUEST);
		
		var request = put("/players/2/role")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("newRole: cannot be GUEST")));
	}
	
	@Test
	@WithUserDetails(value = "nick4")
	public void updateRole_givenModeratorAsCurrent_returnsExceptionDto() throws Exception {		
		var dto = new PlayerRoleUpdateDto();
		dto.setNewRole(Role.HELPER);
		
		var request = put("/players/2/role")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is("Access is denied")));
	}
	
	@Test
	@WithUserDetails(value = "nick1")
	public void updateRole_givenHeadAdminAsCurrentAndNewHeadAdminRole_updatesRole() throws Exception {
		var dto = new PlayerRoleUpdateDto();
		dto.setNewRole(Role.HEAD_ADMINISTRATOR);
		
		var request = put("/players/3/role")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isNoContent());
		assertThat(playerRepository.findById(3).get().getRole()).isEqualTo(Role.HEAD_ADMINISTRATOR);
		assertThat(playerRepository.findById(1).get().getRole()).isEqualTo(Role.ADMINISTRATOR);
	}
	
	@Test
	@WithUserDetails(value = "nick2")
	public void updateRole_givenAdminAsCurrentAndNewHeadAdminRole_returnsExceptionDto() throws Exception {		
		var dto = new PlayerRoleUpdateDto();
		dto.setNewRole(Role.HEAD_ADMINISTRATOR);
		
		var request = put("/players/2/role")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isForbidden())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.description", is(
						"Only the head administrator can change an another player's role to the head administrator")));
	}
	
	@Test
	public void updateSecurityData_givenNewData_updatesData() throws Exception {
		var token = securityTokenProvider.generate(5, "email5@konradmaksymilian.pl");
		
		var dto = new PlayerSecurityDataUpdateDto();
		dto.setNewNick("nick8");
		dto.setNewEmail("validEmail@konradmaksymilian.pl");
		dto.setNewPassword("newPassword");
		dto.setSecurityToken(token);
		
		var request = put("/players/5")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isNoContent());
		var player = playerRepository.findByNick("nick8");
		assertThat(player).isPresent();
		assertThat(player.get().getEmail()).isEqualTo("validEmail@konradmaksymilian.pl");
		assertThat(player.get().getPassword()).isEqualTo("newPassword");
	}
	
	@Test
	public void updateSecurityData_givenNonExistingPlayerId_returnsExceptionDto() throws Exception {		
		var dto = new PlayerSecurityDataUpdateDto();
		dto.setNewPassword("newPassword");
		dto.setSecurityToken("12345678901234567");
		
		var request = put("/players/12")
				.param("email", "email65@konradmaksymilian.pl")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.description", is(
						"Cannot find the player with the given id: 12")));
	}
	
	@Test
	public void updateSecurityData_givenNoSavedSecurityToken_returnsExceptionDto() throws Exception {
		securityTokenProvider.generate(6, "nonExistingEmail@konradmaksymilian.pl");
		
		var dto = new PlayerSecurityDataUpdateDto();
		dto.setNewNick("nick8");
		dto.setNewEmail("validEmail@konradmaksymilian.pl");
		dto.setNewPassword("newPassword");
		dto.setSecurityToken("12345678901234567");
		
		var request = put("/players/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.description", is(
						"Cannot find a security token for the player with the given id: 1")));
	}
	
	@Test
	public void updateSecurityData_givenSentTokenNotMatchingSavedToken_returnsExceptionDto() throws Exception {
		var savedToken = securityTokenProvider.generate(5, "email5@konradmaksymilian.pl");
		
		//makes the sent token not equal to the saved one but still a valid token
		var sentToken = savedToken.replace(savedToken.charAt(0), 'a');
		if (sentToken.equals(savedToken)) {
			sentToken = savedToken.replace('a', 'b');
		}
		
		var dto = new PlayerSecurityDataUpdateDto();
		dto.setNewNick("nick5");
		dto.setNewPassword("newPassword");
		dto.setSecurityToken(sentToken);
		
		var request = put("/players/5")
				.param("email", "email5@konradmaksymilian.pl")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(dto))
				.accept(MediaType.APPLICATION_JSON_UTF8);
		
		mockMvc.perform(request)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.description", is("The sent security token does not match the saved token")));
	}
	
	@Test
	@WithMockUser(roles = "HEAD_ADMINISTRATOR")
	public void delete_givenExistingPlayerId_deletesPlayer() throws Exception {		
		mockMvc.perform(delete("/players/2"))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(roles = "HEAD_ADMINISTRATOR")
	public void delete_givenHeadAdminId_returnsExceptionDto() throws Exception {		
		mockMvc.perform(delete("/players/1"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.description", is("Cannot delete the head administrator's account")));
	}
	
	@Test
	@WithMockUser(roles = "HEAD_ADMINISTRATOR")
	public void delete_givenNonExistingPlayerId_returnsExceptionDto() throws Exception {		
		mockMvc.perform(delete("/players/12"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.description", is("Cannot find the player with the given id: 12")));
	}
	
	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void delete_givenAdminAsCurrent_returnsExceptionDto() throws Exception {		
		mockMvc.perform(delete("/players/5"))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.description", is("Access is denied")));
	}
	
	@TestConfiguration
	public static class IntegrationTestConfig {
		
		@Bean
		@Primary
		public PasswordEncoder passwordEncoder() {
			return new PasswordEncoder() {
				@Override
				public String encode(CharSequence rawPassword) {
					return rawPassword.toString();
				}

				@Override
				public boolean matches(CharSequence rawPassword, String encodedPassword) {
					return rawPassword.equals(encodedPassword);
				}
			};
		}
	}
	
}