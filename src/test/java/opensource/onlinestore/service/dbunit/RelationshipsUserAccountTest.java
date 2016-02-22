package opensource.onlinestore.service.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.AccountEntity;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.service.AccountService;
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

import static junit.framework.TestCase.assertEquals;


/**
 * Created by malex on 02.02.16.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class RelationshipsUserAccountTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml"})
    public void testGetAccountForUser() {
        // given
        AccountEntity accountEntity = accountService.get(1L);

        // when
        UserEntity userEntity = userService.get(1L);

        // then
        assertEquals(accountEntity, userEntity.getAccount());
    }
}
