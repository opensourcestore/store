package opensource.onlinestore.service.logic;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.AccountEntity;
import opensource.onlinestore.model.util.AccountEntityUtil;
import opensource.onlinestore.service.AccountService;
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
 * Created by malex on 01.02.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class AccountServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private AccountService service;

    @Test
    @Rollback
    public void testCreate() {
        // given
        AccountEntity expectEntity = AccountEntityUtil.createAccountEntity();

        // when
        AccountEntity actualEntity = service.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testUpdate() {
        // given
        //1.1 create entity
        AccountEntity expectEntity = service.save(AccountEntityUtil.createAccountEntity());
        //1.2 modify entity
        expectEntity.setAmount(3.33);
        expectEntity.setName("wWwWwW");

        // when
        AccountEntity actualEntity = service.update(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testDelete() {
        // given
        AccountEntity expectEntity = service.update(AccountEntityUtil.createAccountEntity());

        // when
        service.delete(expectEntity.getId());

        //then
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    @Rollback
    public void testFindById() {
        // given
        AccountEntity expectEntity = service.update(AccountEntityUtil.createAccountEntity());

        // when
        AccountEntity actualEntity = service.get(expectEntity.getId());

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testFindAll() {
        // given
        List<AccountEntity> expectEntityList = AccountEntityUtil.createAccountEntityList();
        for (AccountEntity entity : expectEntityList) {
            service.save(entity);
        }

        // when
        List<AccountEntity> actualEntityList = service.getAll();

        // then
        assertFalse(actualEntityList.isEmpty());
        assertEquals(expectEntityList, expectEntityList);
    }

}
