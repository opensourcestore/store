package opensource.onlinestore.service.interaction;

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
public class CreateUserMessageTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    private UserEntity user;

    private int countMessage = 5;


    @Before
    public void init() {
        user = userService.save(UserEntityUtil.createUser());
    }

    @Test
    @Rollback
    public void testCreateUserMessage() {
        // given
        MessageEntity expectMessageEntity = messageEntity();

        //2.1. Add user to message
        expectMessageEntity.setUser(user);

        // when
        MessageEntity actualMessageEntity = messageService.save(expectMessageEntity);

        //then
        assertNotNull(actualMessageEntity);
        assertEquals(expectMessageEntity, actualMessageEntity);
    }

    @Test
    @Rollback
    public void testCreateUserMessages() {
        // given
        List<MessageEntity> expectMessageList = messageEntityList();
        for (MessageEntity entity : expectMessageList) {
            messageService.save(entity);
        }

        // when
        List<MessageEntity> actualMessageList = messageService.getAll();

        //then
        assertNotNull(actualMessageList);
        assertEquals(actualMessageList.size(), countMessage);
        assertEquals(expectMessageList, actualMessageList);
        assertEquals(userService.getAll().size(), 1);
    }


    @Test
    @Rollback
    public void testCreateUserAfterDeleteMessage() {
        // given
        MessageEntity messageEntity = messageService.save(messageEntity());

        // when
        messageService.delete(messageEntity.getId());

        //then
        //4.1 get user
        UserEntity actualUser = userService.get(user.getId());
        assertNotNull(actualUser);
        assertEquals(userService.getAll().size(), 1);

        //4.2 get message
        List<MessageEntity> messageServices = messageService.getAll();
        assertTrue(messageServices.isEmpty());
    }

    private MessageEntity messageEntity() {
        MessageEntity entity = new MessageEntity();
        entity.setMessage("Самый крутой телефон. Всем советую!!!");
        entity.setMessageType(MessageType.SUPPORT);
        entity.setUser(user);
        return entity;
    }

    private List<MessageEntity> messageEntityList() {
        List<MessageEntity> list = new ArrayList<>();

        for (int i = 0; i < countMessage; i++) {
            MessageEntity entity = new MessageEntity();
            entity.setMessage("Самый крутой телефон. Всем советую!!!" + i);
            entity.setMessageType(MessageType.SUPPORT);
            entity.setUser(user);
            list.add(entity);
        }
        return list;
    }

}
