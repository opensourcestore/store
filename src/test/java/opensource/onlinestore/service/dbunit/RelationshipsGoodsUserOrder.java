package opensource.onlinestore.service.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.entity.*;
import opensource.onlinestore.model.enums.DeliveryType;
import opensource.onlinestore.model.enums.OrderStatus;
import opensource.onlinestore.model.util.UserEntityUtil;
import opensource.onlinestore.service.GoodsService;
import opensource.onlinestore.service.OrderService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by malex on 13.02.16.
 */

/**
 * Отчет по тесту:
 *
 * Реализовано:
 *  - создание пустой OrderEntity с User'ом
 *  - создание OrderEntity с User'ом (незарегистированный пользоваьтель)
 *  - не возможно создать OrderEntity без User'a
 *  - сохранение пустой OrderEntity c оформившим User'ом в БД
 *  - наполнение OrderEntity товарами и сохранение в БД
 *  - изменение OrderEntity ( OrderStatus, DeliveryType )
 *  - получение у OrderEntity: UserEntity, List<GoodsEntity>
 *  - изменение OrderEntity на влияет на UserEntity
 *  - удаление OrderEntity (всех или одного из списка) не нарушайт и не изменяет UserEntity
 *
 * Не реализованл:
 *  - получение у UserEntity  лист -  List<OrderEntity>  (при данной связи OTM он пустой)
 *
 * */


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class RelationshipsGoodsUserOrder extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:categories.xml", "classpath:goods.xml"})
    public void testCreateOrder() {
        /**
            Этапы теста:
            - UserEntity создает пустую OrderEntity.
            - Сохранение пустой OrderEntity в БД
         */

        // given
        // 1. get User's
        UserEntity user = userService.get(1L);

        // 2. create Order -> empty basket
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStartDate(new Date());
        order.setOrderStatus(OrderStatus.BUCKET);
        order.setDeliveryType(DeliveryType.EXWORKS);

        //when
        OrderEntity actualOrder = orderService.save(order);

        // then
        assertNotNull(actualOrder);
        assertEquals(order, actualOrder);
        assertEquals(user, actualOrder.getUser());
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:categories.xml", "classpath:goods.xml"})
    public void testCreateOrderNewUser() {
        /**
         Этапы теста:
         - Новый не зарегистрированный UserEntity создает OrderEntity.
         - Сохранение OrderEntity в БД
         */

        // given
        // 1. get User's
        UserEntity user = UserEntityUtil.createUser();
        user.setUsername("Неизвестный писатель");
        user.setEmail("trata@tarta.com");

        // 2. create Order -> empty basket
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStartDate(new Date());
        order.setOrderStatus(OrderStatus.BUCKET);
        order.setDeliveryType(DeliveryType.EXWORKS);

        //when
        OrderEntity actualOrder = orderService.save(order);

        // then
        assertNotNull(actualOrder);
        assertEquals(order, actualOrder);
        assertEquals(user, actualOrder.getUser());
        assertTrue(userService.getAll().contains(user));
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:categories.xml", "classpath:goods.xml"})
    public void testCreateOrderWithoutUser() {
        /**
         Этапы теста:
         - Создание OrderEntity в БД без UserEntity
         - Exception --->>> DataIntegrityViolationException
         */

        // given
        OrderEntity order = new OrderEntity();
        order.setStartDate(new Date());
        order.setOrderStatus(OrderStatus.BUCKET);
        order.setDeliveryType(DeliveryType.EXWORKS);

        //when
        OrderEntity actualOrder = orderService.save(order);

        // then
        assertNotNull(actualOrder);
        assertEquals(order, actualOrder);
    }


    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:categories.xml", "classpath:goods.xml"})
    public void testCreateOrderAddGoods() {
        /**
         Этапы теста:
         - UserEntity создает OrderEntity.
         - Добавляет 3и GoodsEntity в OrderEntity
         - Сохраняет OrderEntity в БД
         */

        // given
        // 1. get User's
        UserEntity user = userService.get(1L);

        // 2. create Order -> empty basket
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStartDate(new Date());
        order.setOrderStatus(OrderStatus.BUCKET);
        order.setDeliveryType(DeliveryType.EXWORKS);

        // 3. get goods
        GoodsEntity entity1 = goodsService.get(1L);
        GoodsEntity entity2 = goodsService.get(2L);
        GoodsEntity entity3 = goodsService.get(3L);

        List<GoodsEntity> list = new ArrayList<>();
        list.add(entity1);
        list.add(entity2);
        list.add(entity3);

        // 4. set list GoodsEntity's
        order.setGoods(list);

        //when
        OrderEntity actualOrder = orderService.save(order);

        // then
        assertNotNull(actualOrder);
        assertEquals(list, actualOrder.getGoods());
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:categories.xml", "classpath:goods.xml"})
    public void testCreateOrderAddGoodsCOURIER() {
        /**
         Этапы теста:
         - UserEntity создает OrderEntity.
         - Добавляет 3и GoodsEntity в OrderEntity
         - Сохраняет OrderEntity в БД
         - Вытаскиваем OrderEntity, меняем параметры OrderStatus, DeliveryType
         - update OrderEntity в БД
         */

        // given
        // 1. get User's
        UserEntity user = userService.get(1L);

        // 2. create Order -> empty basket
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStartDate(new Date());
        order.setOrderStatus(OrderStatus.BUCKET);
        order.setDeliveryType(DeliveryType.EXWORKS);

        // 3. get goods
        GoodsEntity entity1 = goodsService.get(1L);
        GoodsEntity entity2 = goodsService.get(2L);
        GoodsEntity entity3 = goodsService.get(3L);

        List<GoodsEntity> list = new ArrayList<>();
        list.add(entity1);
        list.add(entity2);
        list.add(entity3);

        order.setGoods(list);

        OrderEntity exceptOrder_01 = orderService.save(order);

        //when
        // 4. get OrderEntity
        OrderEntity exceptOrder = orderService.get(exceptOrder_01.getId());
        // 5. OrderEntity set new OrderStatus
        exceptOrder.setOrderStatus(OrderStatus.FINISHED);
        // 6. OrderEntity set new DeliveryType
        exceptOrder.setDeliveryType(DeliveryType.COURIER);
        // 7. update OrderEntity
        OrderEntity actualOrder = orderService.update(exceptOrder);

        // then
        assertNotNull(actualOrder);
        assertEquals(exceptOrder, actualOrder);
        assertEquals(list, actualOrder.getGoods());
        assertEquals(user, actualOrder.getUser());
    }

    @Test
    @Rollback
    @DatabaseSetup({"classpath:accounts.xml", "classpath:users.xml", "classpath:categories.xml", "classpath:goods.xml"})
    public void testGetOrderUser() {
        /**
         Этапы теста:
         - UserEntity создает OrderEntity.
         - Сохраняет OrderEntity в БД
         - Получайм UserEntity и у него пустой лист  List<OrderEntity> //TODO  OTO bidirectional list == null -> custom query!!!
         */

        // given
        // 1. get User's
        UserEntity user = userService.get(1L);

        // 2. create Order -> empty basket
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStartDate(new Date());
        order.setOrderStatus(OrderStatus.BUCKET);
        order.setDeliveryType(DeliveryType.EXWORKS);

        orderService.save(order);

        //when
        UserEntity actualUser = userService.get(user.getId());
        List<OrderEntity> actualOrders = actualUser.getOrders(); //TODO  OTO bidirectional list == null

        // then
        assertNotNull(actualOrders);
        assertEquals(user, actualUser);
    }

}
