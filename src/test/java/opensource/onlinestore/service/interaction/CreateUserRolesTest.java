package opensource.onlinestore.service.interaction;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.enums.Role;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.model.util.UserEntityUtil;
import opensource.onlinestore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by malex on 03.02.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class CreateUserRolesTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService service;

    @Test
    @Rollback
    public void testCreateUsersRoles() {
        // given
        UserEntity expectEntity = UserEntityUtil.createUser();
        Set<Role> expectRoles = fullRoles();

        // when
        expectEntity.setRoles(expectRoles);
        UserEntity actualEntity = service.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectRoles, actualEntity.getRoles());
        assertEquals(service.getAll().size(), 1);
    }

    @Test
    @Rollback
    public void testDeleteUsersRole() {
        // given
        //1.1 create User
        UserEntity user = UserEntityUtil.createUser();
        //1.2 create user's roles
        Set<Role> roles = fullRoles();
        user.setRoles(roles);
        //1.3 Save User to db
        UserEntity userEntity = service.save(user);

        // when
        //2.1 get user
        UserEntity expectEntity = service.get(userEntity.getId());
        //2.2 remove role -> ADMIN
        expectEntity.getRoles().remove(Role.ADMIN);
        //2.3 Update user
        UserEntity actualEntity = service.update(userEntity);
        //2.4 remove
        roles.remove(Role.ADMIN);

        //then
        assertNotNull(actualEntity);
        assertEquals(roles, actualEntity.getRoles());
        assertEquals(service.getAll().size(), 1);
    }

    private Set<Role> fullRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.CONTENT_MANAGER);
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        roles.add(Role.SUPPORT);
        return roles;
    }

}
