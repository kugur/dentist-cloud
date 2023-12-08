package com.kolip.dentist.authenticationservice.repostiory;

import com.kolip.dentist.authenticationservice.model.CustomUser;
import com.kolip.dentist.authenticationservice.model.Role;
import com.kolip.dentist.authenticationservice.repository.CustomUserRepository;
import com.kolip.dentist.authenticationservice.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integrationtest")
public class CustomUserRepositoryTest {

    @Autowired
    CustomUserRepository customUserRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    public void deneme() {
        CustomUser denemeUser = new CustomUser();
        denemeUser.setEmail("ememe@gmail.com");
        denemeUser.setPassword("1234");
        denemeUser.setUsername("deneme@gmail.com");
        CustomUser savedUser = customUserRepository.save(denemeUser);
        assertNotNull(savedUser);
    }

    @Test
    public void save_UserWithRole_ShouldPersistUserAndRole() {
        //Initializate
        persistRoles();
        Role userRole = roleRepository.findByName("ROLE_USER");
        CustomUser user = new CustomUser();
        user.setUsername("deneme@gmail.com");
        user.setPassword("1234");
        user.setEmail("deneme@gmaiol.com");
        user.setRoles(Set.of(userRole));

        //Run test
        CustomUser savedUser = customUserRepository.saveAndFlush(user);
        entityManager.clear();
        Optional<CustomUser> fetchedUserOptional = customUserRepository.findById(savedUser.getId());

        //Verify Result
        assertTrue(fetchedUserOptional.isPresent());
        CustomUser fetchedUser = fetchedUserOptional.get();
        assertNotNull(fetchedUser);
        assertNotNull(fetchedUser.getRoles());
        assertNotNull(fetchedUser.getAuthorities());
        assertEquals(1, fetchedUser.getRoles().size());
        assertEquals(1, fetchedUser.getAuthorities().size());
    }

    @Test
    public void save_UpdateUserRole_ShouldNotDeleteTheRole() {
        //Initialize
        persistRoles();
        Role userRole = roleRepository.findByName("ROLE_USER");
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        CustomUser user = createUser(userRole, adminRole);
        CustomUser persistedUser = customUserRepository.saveAndFlush(user);
        Long persistedUserId = persistedUser.getId();
        entityManager.clear();

        //Run Test
        Optional<CustomUser> fetchedUserOptional = customUserRepository.findById(persistedUserId);
        assertTrue(fetchedUserOptional.isPresent());
        CustomUser fetchedUser = fetchedUserOptional.get();

        fetchedUser.deleteRole(userRole);
        customUserRepository.saveAndFlush(fetchedUser);
        entityManager.clear();

        //VerifyResult
        Optional<CustomUser> updatedUser = customUserRepository.findById(persistedUserId);
        assertTrue(updatedUser.isPresent());
        assertEquals(1, updatedUser.get().getRoles().size(), "one role removed, should have one role.");
        assertNotNull(roleRepository.findByName("ROLE_USER"));
    }

    private void persistRoles() {
        Role userRole = new Role("ROLE_USER");
        roleRepository.saveAndFlush(userRole);

        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.saveAndFlush(adminRole);
    }

    private CustomUser createUser(Role... role) {
        CustomUser user = new CustomUser();
        user.setUsername("deneme@gmail.com");
        user.setPassword("1234");
        user.setEmail("deneme@gmaiol.com");
        user.setRoles(Set.of(role));
        return user;
    }
}
