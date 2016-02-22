package opensource.onlinestore.service.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.model.entity.GoodsEntity;
import opensource.onlinestore.model.util.CategoryEntityUtil;
import opensource.onlinestore.model.util.GoodsEntityUtil;
import opensource.onlinestore.service.CategoryService;
import opensource.onlinestore.service.GoodsService;
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

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by malex on 04.02.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class RelationshipsGoodsCategoriesTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;


    @Test
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testGetCategory_01() {
        // given
        // <categories id="1" name="Телефоны"/>
        CategoryEntity categoryEntity = categoryService.get(1L);

        //  <goods id="1, 2, 3" />
        List<GoodsEntity> expectList = new ArrayList<>();
        expectList.add(goodsService.get(1L));
        expectList.add(goodsService.get(2L));
        expectList.add(goodsService.get(3L));

        //when
        List<GoodsEntity> actualList = goodsService.getAll().stream().filter(entity -> entity.getCategory().equals(categoryEntity)).collect(Collectors.toList());

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testGetCategory_02() {
        // given
        // <categories id="2" name="Планшеты"/>
        CategoryEntity categoryEntity = categoryService.get(2L);

        //  <goods id="4, 5" />
        List<GoodsEntity> expectList = new ArrayList<>();
        expectList.add(goodsService.get(4L));
        expectList.add(goodsService.get(5L));

        //when
        List<GoodsEntity> actualList = goodsService.getAll().stream().filter(entity -> entity.getCategory().equals(categoryEntity)).collect(Collectors.toList());

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testGetCategory_03() {
        // given
        // <categories id="3" name="Ноутбуки"/>
        CategoryEntity categoryEntity = categoryService.get(3L);

        //  <goods id="6, 7" />
        List<GoodsEntity> expectList = new ArrayList<>();
        expectList.add(goodsService.get(6L));
        expectList.add(goodsService.get(7L));

        //when
        List<GoodsEntity> actualList = goodsService.getAll().stream().filter(entity -> entity.getCategory().equals(categoryEntity)).collect(Collectors.toList());

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testGetCategory_04() {
        // given
        // <categories id="4" name="Фото"/>
        CategoryEntity categoryEntity = categoryService.get(4L);

        //  <goods id="8, 9, 10" />
        List<GoodsEntity> expectList = new ArrayList<>();
        expectList.add(goodsService.get(8L));
        expectList.add(goodsService.get(9L));
        expectList.add(goodsService.get(10L));

        //when
        List<GoodsEntity> actualList = goodsService.getAll().stream().filter(entity -> entity.getCategory().equals(categoryEntity)).collect(Collectors.toList());

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testGetCategory_05() {
        // given
        // <categories id="5" name="Бытовая техника"/>
        CategoryEntity categoryEntity = categoryService.get(5L);

        //  <goods id="11, 12, 13" />
        List<GoodsEntity> expectList = new ArrayList<>();
        expectList.add(goodsService.get(11L));
        expectList.add(goodsService.get(12L));
        expectList.add(goodsService.get(13L));

        //when
        List<GoodsEntity> actualList = goodsService.getAll().stream().filter(entity -> entity.getCategory().equals(categoryEntity)).collect(Collectors.toList());

        // then
        assertNotNull(actualList);
        assertFalse(actualList.isEmpty());
        assertEquals(expectList, actualList);
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testDeleteGoods() {
        // given
        //1.1 create category
        CategoryEntity categoryEntity = CategoryEntityUtil.createCategory();
        categoryService.save(categoryEntity);

        //1.2 create goods
        GoodsEntity goodsEntity = GoodsEntityUtil.createGoodsEntity();
        goodsEntity.setCategory(categoryEntity);
        Map<String, String> characteristicsMap = new HashMap<>();
        characteristicsMap.put("age", "200");
        goodsEntity.setCharachteristicsFromMap(characteristicsMap);
        GoodsEntity actualGoodsEntity = goodsService.save(goodsEntity);

        //when
        goodsService.delete(actualGoodsEntity.getId());

        // then
        List<CategoryEntity> categoryEntityList = categoryService.getAll();
        assertTrue(categoryEntityList.contains(categoryEntity));
    }

    @Test(expected = ConstraintViolationException.class)
    @Rollback
    @DatabaseSetup({"classpath:categories.xml", "classpath:goods.xml"})
    public void testShouldRaiseConstraintViolationException() {
        // given
        //1.1 create category
        CategoryEntity categoryEntity = CategoryEntityUtil.createCategory();
        categoryService.save(categoryEntity);

        //1.2 create goods
        GoodsEntity goodsEntity = GoodsEntityUtil.createGoodsEntity();
        goodsEntity.setCategory(categoryEntity);
        Map<String, String> characteristicsMap = new HashMap<>();
        characteristicsMap.put("age", "200");
        characteristicsMap.put("strangecharacteristic", "lol");
        goodsEntity.setCharachteristicsFromMap(characteristicsMap);
        goodsService.save(goodsEntity);
    }

}
