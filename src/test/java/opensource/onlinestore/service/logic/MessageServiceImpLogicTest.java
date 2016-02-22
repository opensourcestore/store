package opensource.onlinestore.service.logic;

import opensource.onlinestore.configuration.AppConfigTest;

import opensource.onlinestore.model.entity.MessageEntity;
import opensource.onlinestore.model.enums.MessageType;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.model.util.UserEntityUtil;
import opensource.onlinestore.service.MessageService;
import opensource.onlinestore.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by malex on 29.01.16.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class MessageServiceImpLogicTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    private UserEntity user;

    @Before
    public void init() {
        user = userService.save(UserEntityUtil.createUser());
    }

    @Test
    @Rollback
    public void testCreate() {
        // given
        MessageEntity expectEntity = messageEntity();

        // when
        MessageEntity actualEntity = messageService.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testUpdate() {
        // given
        //1.1 create entity
        MessageEntity expectEntity = messageService.save(messageEntity());
        //1.2 modify entity
        expectEntity.setMessage("Тра та та");
        expectEntity.setMessageType(MessageType.SUPPORT);

        // when
        MessageEntity actualEntity = messageService.update(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testDelete() {
        // given
        MessageEntity entity = new MessageEntity();
        entity.setMessage("Комент к заказу №000000000000111");
        entity.setMessageType(MessageType.OPINION);
        entity.setUser(user);
        MessageEntity expectEntity = messageService.save(entity);

        // when
        messageService.delete(expectEntity.getId());

        //then
        assertTrue(messageService.getAll().isEmpty());
        assertNotNull(userService.get(user.getId()));
    }

    @Test
    @Rollback
    public void testFindById() {
        // given
        MessageEntity entity = new MessageEntity();
        entity.setMessage("Комент к заказу №000000000000111");
        entity.setMessageType(MessageType.OPINION);
        entity.setUser(user);
        MessageEntity expectEntity = messageService.save(entity);

        // when
        MessageEntity actualEntity = messageService.get(expectEntity.getId());

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testFindAll() {
        // given
        List<MessageEntity> expectEntityList = messageEntityList();
        for (MessageEntity entity : expectEntityList) {
            messageService.save(entity);
        }

        // when
        List<MessageEntity> actualEntityList = messageService.getAll();

        // then
        assertFalse(actualEntityList.isEmpty());
        assertEquals(expectEntityList, expectEntityList);
    }

    private List<MessageEntity> messageEntityList() {
        List<MessageEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MessageEntity entity = new MessageEntity();
            entity.setMessage("Комент к заказу №000000000000111_" + i);
            entity.setMessageType(MessageType.OPINION);
            entity.setUser(user);
            list.add(entity);
        }
        return list;
    }

    private MessageEntity messageEntity(){
        MessageEntity entity = new MessageEntity();
        entity.setMessage("Комент к заказу №000000000000111");
        entity.setMessageType(MessageType.OPINION);
        entity.setUser(user);
        return entity;
    }

}
