package opensource.onlinestore.service.logic;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.model.util.CategoryEntityUtil;
import opensource.onlinestore.service.CategoryService;
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
public class CategoryServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private CategoryService service;

    @Test
    @Rollback
    public void testCreate() {
        // given
        CategoryEntity expectEntity = CategoryEntityUtil.createCategory();

        // when
        CategoryEntity actualEntity = service.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testUpdate() {
        // given
        //1.1 create entity
        CategoryEntity expectEntity = service.save(CategoryEntityUtil.createCategory());
        //1.2 modify entity
        expectEntity.setName("cat002");

        // when
        CategoryEntity actualEntity = service.update(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testDelete() {
        // given
        CategoryEntity expectEntity = service.save(CategoryEntityUtil.createCategory());

        // when
        service.delete(expectEntity.getId());

        //then
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    @Rollback
    public void testFindById() {
        // given
        CategoryEntity expectEntity = service.save(CategoryEntityUtil.createCategory());

        // when
        CategoryEntity actualEntity = service.get(expectEntity.getId());

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }


    @Test
    @Rollback
    public void testFindAll() {
        // given
        List<CategoryEntity> expectEntityList =  CategoryEntityUtil.createCategoryEntityList();
        for (CategoryEntity entity : expectEntityList) {
            service.save(entity);
        }

        // when
        List<CategoryEntity> actualEntityList = service.getAll();

        // then
        assertFalse(actualEntityList.isEmpty());
        assertEquals(expectEntityList, expectEntityList);

    }

}
