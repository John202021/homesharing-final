package gr.hua.dit.HomeSharing;

import gr.hua.dit.HomeSharing.entities.Role;
import gr.hua.dit.HomeSharing.entities.User;
import gr.hua.dit.HomeSharing.repositories.HomeCharacteristicsRepository;
import gr.hua.dit.HomeSharing.repositories.RoleRepository;
import gr.hua.dit.HomeSharing.repositories.UserRepository;
import gr.hua.dit.HomeSharing.services.FileStorageService;
import gr.hua.dit.HomeSharing.entities.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class HomeSharingApplicationTests {
	@MockitoBean
	private FileStorageService fileStorageService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private HomeCharacteristicsRepository homeCharacteristicsRepository;

	private User adminUser;

	@BeforeEach
	void setUp() {
		Role adminRole = new Role();
		adminRole.setName("ROLE_ADMIN");
		roleRepository.save(adminRole);

		adminUser = new User();
		adminUser.setFirstName("Admin");
		adminUser.setLastName("User");
		adminUser.setEmail("admin@example.com");
		adminUser.setPassword("adminpass");
		adminUser.setPhoneNumber("1111111111");
		adminUser.setBirthDate(LocalDate.of(1985, 1, 1));
		adminUser.getRoles().add(adminRole);

		userRepository.save(adminUser);
	}

	@Test
	void showRegisterRenterForm() throws Exception {
		mockMvc.perform(get("/register-renter"))
				.andExpect(status().isOk())
				.andExpect(view().name("auth/register_renter"));
	}

	@Test
	void registerRenter() throws Exception {
		mockMvc.perform(post("/register-renter")
						.with(csrf())
						.param("firstName", "Jane")
						.param("lastName", "Doe")
						.param("email", "jane@example.com")
						.param("password", "secret")
						.param("phoneNumber", "0987654321")
						.param("birthDate", "1995-05-20"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void listUsersForAdmin() throws Exception {
		CustomUserDetails adminDetails = new CustomUserDetails(
				adminUser.getId(),                     // int id
				adminUser.getEmail(),                 // username
				adminUser.getPassword(),             // password
				List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))  // authorities
		);

		mockMvc.perform(get("/users").with(user(adminDetails)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andExpect(view().name("user/users"));
	}
}
