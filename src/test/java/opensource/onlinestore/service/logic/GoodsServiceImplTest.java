package opensource.onlinestore.service.logic;

import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.model.entity.GoodsEntity;
import opensource.onlinestore.model.util.CategoryEntityUtil;
import opensource.onlinestore.service.CategoryService;
import opensource.onlinestore.service.GoodsService;
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
 * Created by malex on 01.02.16.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class GoodsServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    private CategoryEntity category;

    @Before
    public void init() {
        category = categoryService.save(CategoryEntityUtil.createCategory());
    }

    @Test
    @Rollback
    public void testCreate() {
        // given
        GoodsEntity expectEntity = createEntity();

        // when
        GoodsEntity actualEntity = goodsService.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testUpdate() {
        // given
        //1.1 create entity
        GoodsEntity expectEntity = goodsService.save(createEntity());
        //1.2 modify entity
        expectEntity.setName("NewField");
        expectEntity.setArticle("11111");
        expectEntity.setPrice(11.11);
        expectEntity.setProducer("Китай");

        // when
        GoodsEntity actualEntity = goodsService.update(expectEntity);

        //then
        //3.1 NotNull Entity
        assertNotNull(actualEntity);
        //3.2 Equals Entity
        assertEquals(expectEntity, actualEntity);
        //3.3 Comparison of the number of entities.
        assertEquals(goodsService.getAll().size(), 1);
    }

    @Test
    @Rollback
    public void testDelete() {
        // given
        GoodsEntity expectEntity = goodsService.save(createEntity());

        // when
        goodsService.delete(expectEntity.getId());

        //then
        assertTrue(goodsService.getAll().isEmpty());
        assertNotNull(categoryService.get(category.getId())); //TODO : AssertionError
    }

    @Test
    @Rollback
    public void testFindById() {
        // given
        GoodsEntity expectEntity = goodsService.save(createEntity());

        // when
        GoodsEntity actualEntity = goodsService.get(expectEntity.getId());

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testFindAll() {
        // given
        List<GoodsEntity> expectEntityList = goodsEntityList();
        for (GoodsEntity entity : expectEntityList) {
            goodsService.save(entity);
        }

        // when
        List<GoodsEntity> actualEntityList = goodsService.getAll();

        // then
        assertFalse(actualEntityList.isEmpty());
        assertEquals(expectEntityList, expectEntityList);
    }

    private GoodsEntity createEntity() {
        GoodsEntity entity = new GoodsEntity();
        entity.setName("Nokia 3110");
        entity.setArticle("Телефон");
        entity.setCounts(1L);
        entity.setCategory(null);
        entity.setPrice(99.01);
        entity.setProducer("Китай");
        entity.setCategory(category);
        return entity;
    }

    private List<GoodsEntity> goodsEntityList() {
        List<GoodsEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GoodsEntity entity = new GoodsEntity();
            entity.setName("Nokia 3110_" + i);
            entity.setArticle("Телефон_" + i);
            entity.setCounts(1L);
            entity.setCategory(null);
            entity.setPrice(99.01 + i);
            entity.setProducer("Китай_" + i);
            entity.setCategory(category);
            list.add(entity);
        }
        return list;
    }
}
