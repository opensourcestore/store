package opensource.onlinestore.service.interaction;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.AccountEntity;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.model.util.AccountEntityUtil;
import opensource.onlinestore.model.util.UserEntityUtil;
import opensource.onlinestore.service.AccountService;
import opensource.onlinestore.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by malex on 14.02.16.
 */


/**
 * Отчет по тесту:
 *
 * Реализовано:
 *  - создание нового пользователя без аккаунта
 *  - создание пользователю платежного аккаунта (для обеспечения двух направлленого взаимодействия между сущ, нужно выполнить опред условие 1)
 *  - получение пользователем своего аккаунта
 *  - получение по аккаунту пользователя
 *  - изменение пользователем своего аккаунта
 *  - удаление пользователя и его аккаунта
 *
 *  Не реализовано:
 *  - удаления у пользователя платежного аккаунта, в данный момент при удалении ни чего не происходит.
 *  - в данной двухнаправлленой связи возможно либо полное удалени или не удаление))) сущностей
 *  - юзер всегда привязан к аккаунту
 *
 * */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class RelationshipsUserAccountTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    @Test
    @Rollback
    public void testCreateUserAccount() {
        /**
         Этапы теста:
         - Создание обычного User без Account
         - Создание User'у нового Account (Account создаеться со своей стороны, добавление Аккаунта со стороны User -> Exception)
         - Добавление в Account нужно только существующего в БД User'а. (Иначе не будет bidirectional mapping связи с сущностью)
         - Для того, чтобы существовала связь bidirectional между сущностями необходимо выполнить условие :1 придобавлении сущности
         */

        // given
        UserEntity expectUser = UserEntityUtil.createUser();
        UserEntity actualUser = userService.save(expectUser);

        // when
        AccountEntity expectAccount = AccountEntityUtil.createAccountEntity();
        expectAccount.setUser(actualUser);
        actualUser.setAccount(expectAccount);   //  : 1 условие

        AccountEntity actualAccount = accountService.save(expectAccount);

        // then
        assertNotNull(actualAccount);
        assertEquals(expectAccount, actualAccount);
    }

    @Test
    @Rollback
    public void testGetUsersAccount() {
        /**
         Этапы теста:
         - Создание обычного User без Account
         - Создание User'у нового Account
         - Получение User's -> Аккаунта
         * */

        // given
        UserEntity user = UserEntityUtil.createUser();
        UserEntity expectUser = userService.save(user);

        AccountEntity account = AccountEntityUtil.createAccountEntity();
        account.setUser(expectUser);
        expectUser.setAccount(account);
        AccountEntity expectAccount = accountService.save(account);

        // when
        UserEntity actualUser = userService.get(expectUser.getId());
        AccountEntity actualAccount = actualUser.getAccount();

        // then
        assertNotNull(actualAccount);
        assertEquals(expectAccount, actualAccount);
    }

    @Test
    @Rollback
    public void testGetAccountsUser(){
        /**
         Этапы теста:
         - Создание обычного User без Account
         - Создание User'у нового Account
         - Получение у Account -> User'a
         * */
        // given
        UserEntity user = UserEntityUtil.createUser();
        UserEntity expectUser = userService.save(user);

        AccountEntity account = AccountEntityUtil.createAccountEntity();
        account.setUser(expectUser);
        expectUser.setAccount(account);
        AccountEntity expectAccount = accountService.save(account);

        // when
        AccountEntity actualAccount = accountService.get(expectAccount.getId());
        UserEntity actualUser = actualAccount.getUser();

        // then
        assertNotNull(actualUser);
        assertEquals(expectUser, actualUser);
    }

    @Test
    @Rollback
    public void testUpdateUsersAccount() {
        /**
         Этапы теста:
         - Создание обычного User без Account
         - Создание User'у нового Account
         - Изменение User'ом своего Account
         * */

        // given
        UserEntity user = UserEntityUtil.createUser();
        UserEntity expectUser = userService.save(user);

        AccountEntity account = AccountEntityUtil.createAccountEntity();
        account.setUser(expectUser);
        expectUser.setAccount(account);
        accountService.save(account);

        // when
        UserEntity actualUser = userService.get(expectUser.getId());
        AccountEntity actualAccount = actualUser.getAccount();
        actualAccount.setAmount(2555.02);
        actualAccount.setName("NeWAkkkk");

        UserEntity updateUser = userService.update(actualUser);

        // then
        assertNotNull(updateUser);
        assertEquals(actualUser, updateUser);
    }

    @Test
    @Rollback
    public void testDeleteUsers() {
        /**
         Этапы теста:
         - Создание обычного User без Account
         - Создание User'у нового Account
         - при удалении User'ом удаляеться его Account (полное удаление)
         * */

        // given
        UserEntity user = UserEntityUtil.createUser();
        UserEntity expectUser = userService.save(user);

        AccountEntity account = AccountEntityUtil.createAccountEntity();
        account.setUser(expectUser);
        expectUser.setAccount(account);
        accountService.save(account);

        // when
        UserEntity actualUser = userService.get(expectUser.getId());
        userService.delete(actualUser.getId());

        // then
        assertFalse(userService.getAll().contains(actualUser));
        assertFalse(accountService.getAll().contains(account));
    }

}
