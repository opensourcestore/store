package opensource.onlinestore.service.logic;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.service.UserService;
import opensource.onlinestore.model.util.UserEntityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by malex on 26.01.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class UserServiceImplLogicTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService service;

    @Test
    @Rollback
    public void testCreate() {
        // given
        UserEntity expectEntity = UserEntityUtil.createUser();

        // when
        UserEntity actualEntity = service.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testUpdate() {
        // given
        //1.1 create entity
        UserEntity expectEntity = service.save(UserEntityUtil.createUser());
        //1.2 modify entity
        expectEntity.setPassword("123456");
        expectEntity.setFirstName("XxX");

        // when
        UserEntity actualEntity = service.update(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testDelete() {
        // given
        UserEntity expectEntity = service.save(UserEntityUtil.createUser());

        // when
        service.delete(expectEntity.getId());

        //then
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    @Rollback
    public void testFindById() {
        // given
        UserEntity expectEntity = service.save(UserEntityUtil.createUser());

        // when
        UserEntity actualEntity = service.get(expectEntity.getId());

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }


    @Test
    @Rollback
    public void testFindAll() {
        // given
        List<UserEntity> expectEntityList = UserEntityUtil.createUsers();
        for (UserEntity entity : expectEntityList) {
            service.save(entity);
        }

        // when
        List<UserEntity> actualEntityList = service.getAll();

        // then
        assertFalse(actualEntityList.isEmpty());
        assertEquals(expectEntityList, expectEntityList);
    }


}
