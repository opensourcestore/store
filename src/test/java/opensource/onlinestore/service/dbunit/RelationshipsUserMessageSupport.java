package opensource.onlinestore.service.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.MessageEntity;
import opensource.onlinestore.model.enums.MessageType;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.model.util.UserEntityUtil;
import opensource.onlinestore.service.MessageService;
import opensource.onlinestore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;


/**
 * Created by malex on 17.02.16.
 */

/**
 * Отчет по тесту:
 *
 * Реализовано:
 * - создание support MessageEntity с User'ом (незарегистированный пользоваьтель)
 * - не возможно создать MessageEntity без User'a
 * - сохранение support MessageEntity c сущ. User'ом в БД
 * - изменение MessageEntity ( MessageType, message )
 * - получение у MessageEntity: UserEntity
 * - изменение MessageEntity на влияет на UserEntity
 * - удаление MessageEntity (всех или одного из списка) не нарушайт и не изменяет UserEntity
 *
 * Не реализованл:
 * - получение у UserEntity  лист -  List<MessageEntity>  (при данной связи OTM он пустой)
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class RelationshipsUserMessageSupport extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testCreateMessageSupport() {
        // given
        UserEntity user = userService.get(1L);

        MessageEntity message = new MessageEntity();
        message.setMessageType(MessageType.SUPPORT);
        message.setMessage("проблема с телефоном NoKia 3110. Постоянно виснет.");
        message.setUser(user);

        // when
        MessageEntity actualMessage = messageService.save(message);  // создание support MessageEntity с User'ом

        // then
        assertNotNull(actualMessage);
        assertEquals(message, actualMessage);
        assertEquals(user, actualMessage.getUser()); // получение у MessageEntity: UserEntity
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testCreateMessageSupportWithoutUser() {

        // given
        MessageEntity message = new MessageEntity();
        message.setMessageType(MessageType.SUPPORT);
        message.setMessage("проблема с телефоном NoKia 3110. Постоянно виснет.");

        // when
        MessageEntity actualMessage = messageService.save(message);  // не возможно создать MessageEntity без User'a

        // then
        assertNotNull(actualMessage);
        assertEquals(message, actualMessage);
    }


    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testCreateMessageSupportNewUser() {

        // given
        UserEntity user = UserEntityUtil.createUser();
        user.setUsername("Неизвестный писатель");
        user.setEmail("trata@tarta.com");

        MessageEntity message = new MessageEntity();
        message.setMessageType(MessageType.SUPPORT);
        message.setMessage("Нужен адрес Вашего сервисного центра");
        message.setUser(user);

        // when
        MessageEntity actualMessage = messageService.save(message);

        // then
        assertNotNull(actualMessage);
        assertEquals(message, actualMessage);
        assertEquals(user, actualMessage.getUser());
        assertTrue(userService.getAll().contains(user));
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testUpdateMessageSupport() {
        // given
        UserEntity user = userService.get(1L);

        MessageEntity message = new MessageEntity();
        message.setMessageType(MessageType.SUPPORT);
        message.setMessage("проблема с телефоном NoKia 3110. Постоянно виснет.");
        message.setUser(user);

        MessageEntity actualMessage = messageService.save(message);  // создание support MessageEntity с User'ом

        // when
        MessageEntity messageEntity = messageService.get(actualMessage.getId());
        messageEntity.setMessage("Проблема решена");
        messageEntity.setMessageType(MessageType.OPINION);
        MessageEntity expectMessage = messageService.update(messageEntity); //изменение MessageEntity ( MessageType, message )

        // then
        assertNotNull(expectMessage);
        assertEquals(messageEntity, expectMessage);
        assertEquals(user, expectMessage.getUser());
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testDeleteMessageSupport() {
        // given
        UserEntity user = userService.get(1L);

        MessageEntity message = new MessageEntity();
        message.setMessageType(MessageType.SUPPORT);
        message.setMessage("проблема с телефоном NoKia 3110. Постоянно виснет.");
        message.setUser(user);
        MessageEntity actualMessage = messageService.save(message);  // создание support MessageEntity с User'ом

        // when
        messageService.delete(actualMessage.getId());

        // then
        assertEquals(messageService.getAll().size(), 0);
        assertTrue(userService.getAll().contains(user));
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testGetMessageListForUser() {
        // given
        UserEntity user = userService.get(1L);

        MessageEntity message = new MessageEntity();
        message.setMessageType(MessageType.SUPPORT);
        message.setMessage("проблема с телефоном NoKia 3110. Постоянно виснет.");
        message.setUser(user);
        MessageEntity actualMessage = messageService.save(message);  // создание support MessageEntity с User'ом

        // when
        UserEntity actualUser = userService.get(user.getId());
        List<MessageEntity> opinions = actualUser.getOpinions(); //TODO  OTO bidirectional list == null

        // then
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

}
