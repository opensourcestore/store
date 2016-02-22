package opensource.onlinestore.service.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.enums.Role;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by malex on 03.02.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class RelationshipsUserRolesTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:users_roles.xml"})
    public void testGetUsersWithAdminRole() {
        // given
        Role role = Role.ADMIN;
        // <users_roles user_id="1" role="ADMIN"/>
        UserEntity expectUser = userService.get(1L);

        // when
        List<UserEntity> userList = userService.getAll();
        List<UserEntity> actualList = new ArrayList<>();
        actualList.addAll(userList.stream().filter(entity -> entity.getRoles().contains(role)).collect(Collectors.toList()));

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectUser, actualList.get(0));
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:users_roles.xml"})
    public void testGetUsersWithContentManagerRole() {
        // given
        Role role = Role.CONTENT_MANAGER;
        // <users_roles user_id="2" role="CONTENT_MANAGER"/>
        // <users_roles user_id="3" role="CONTENT_MANAGER"/>
        // <users_roles user_id="12" role="USER"/>
        List<UserEntity> expectList = new ArrayList<>();
        expectList.add(userService.get(2L));
        expectList.add(userService.get(3L));
        expectList.add(userService.get(12L));

        // when
        List<UserEntity> userList = userService.getAll();
        List<UserEntity> actualList = new ArrayList<>();
        actualList.addAll(userList.stream().filter(entity -> entity.getRoles().contains(role)).collect(Collectors.toList()));

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:users_roles.xml"})
    public void testGetUsersWithSupportRole() {
        // given
        Role role = Role.SUPPORT;
        // <users_roles user_id="2" role="CONTENT_MANAGER"/>
        // <users_roles user_id="3" role="CONTENT_MANAGER"/>
        // <users_roles user_id="12" role="USER"/>
        List<UserEntity> expectList = new ArrayList<>();
        expectList.add(userService.get(4L));
        expectList.add(userService.get(5L));
        expectList.add(userService.get(12L));

        // when
        List<UserEntity> userList = userService.getAll();
        List<UserEntity> actualList = new ArrayList<>();
        actualList.addAll(userList.stream().filter(entity -> entity.getRoles().contains(role)).collect(Collectors.toList()));

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:users_roles.xml"})
    public void testGetUsersWithUserRole() {
        // given
        Role role = Role.USER;
        //<users_roles user_id="3" role="USER"/>
        //<users_roles user_id="6" role="USER"/>
        //<users_roles user_id="7" role="USER"/>
        //<users_roles user_id="8" role="USER"/>
        //<users_roles user_id="9" role="USER"/>
        //<users_roles user_id="10" role="USER"/>
        // <users_roles user_id="12" role="USER"/>
        List<UserEntity> expectList = new ArrayList<>();
        expectList.add(userService.get(3L));
        expectList.add(userService.get(6L));
        expectList.add(userService.get(7L));
        expectList.add(userService.get(8L));
        expectList.add(userService.get(9L));
        expectList.add(userService.get(10L));
        expectList.add(userService.get(12L));

        // when
        List<UserEntity> userList = userService.getAll();
        List<UserEntity> actualList = new ArrayList<>();
        actualList.addAll(userList.stream().filter(entity -> entity.getRoles().contains(role)).collect(Collectors.toList()));

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }


    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:users_roles.xml"})
    public void testGetUsersRoles_01() {
        // given
        // <users_roles user_id="3" role="CONTENT_MANAGER"/>
        // <users_roles user_id="3" role="USER"/>
        Set<Role> actualRoles = new TreeSet<>();
        actualRoles.add(Role.CONTENT_MANAGER);
        actualRoles.add(Role.USER);

        // when
        UserEntity expectUser = userService.get(3L);

        // then
        assertNotNull(expectUser);
        assertEquals(actualRoles, expectUser.getRoles());
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:users_roles.xml"})
    public void testGetUsersRoles_02() {
        // given
        // <users_roles user_id="12" role="CONTENT_MANAGER"/>
        // <users_roles user_id="12" role="USER"/>
        Set<Role> actualRoles = new TreeSet<>();
        actualRoles.add(Role.CONTENT_MANAGER);
        actualRoles.add(Role.USER);
        actualRoles.add(Role.SUPPORT);
        actualRoles.add(Role.ADMIN);

        // when
        UserEntity expectUser = userService.get(12L);

        // then
        assertNotNull(expectUser);
        assertEquals(actualRoles, expectUser.getRoles());
    }
}
