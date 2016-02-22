package opensource.onlinestore.service.impl;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.dto.UserDTO;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.repository.UserRepository;
import opensource.onlinestore.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by orbot on 29.01.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @Test
    public void testShouldRegisterUser() {
        UserDTO registrationDTO = new UserDTO();
        registrationDTO.setUsername("username");
        registrationDTO.setPassword("password");
        registrationDTO.setFirstName("Ivan");
        registrationDTO.setLastName("Ivanov");
        registrationDTO.setEmail("ivanov@megamail.com");
        userService.registerNewUser(registrationDTO);
        UserEntity savedEntity = userRepository.getByEmail("ivanov@megamail.com");
        Assert.assertNotNull(savedEntity);
    }

    @Transactional
    @Test
    public void testShouldReturnToken() {
        UserDTO registrationDTO = new UserDTO();
        registrationDTO.setUsername("username");
        registrationDTO.setPassword("password");
        registrationDTO.setFirstName("Ivan");
        registrationDTO.setLastName("Ivanov");
        registrationDTO.setEmail("ivanov@megamail.com");
        UserEntity userEntity = userService.registerNewUser(registrationDTO);
        String token = userService.authenticateUserAndGetToken(userEntity);
        Assert.assertNotNull(token);
    }

}