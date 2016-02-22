package opensource.onlinestore.service.logic;


import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.enums.DeliveryType;
import opensource.onlinestore.model.entity.OrderEntity;
import opensource.onlinestore.model.enums.OrderStatus;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.model.util.UserEntityUtil;
import opensource.onlinestore.service.OrderService;
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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by malex on 26.01.16.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
public class OrderServiceImplLogicTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private OrderService orderService;

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
        OrderEntity expectEntity = orderEntity();

        // when
        OrderEntity actualEntity = orderService.save(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testUpdate() {
        // given
        //1.1 create entity
        OrderEntity expectEntity = orderService.save(orderEntity());
        //1.2 modify entity
        expectEntity.setOrderStatus(OrderStatus.COLLECTED);
        expectEntity.setAddress("Дом №4");

        // when
        OrderEntity actualEntity = orderService.update(expectEntity);

        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testDelete() {
        // given
        OrderEntity entity = new OrderEntity();
        entity.setAddress("Город 222");
        entity.setDeliveryType(DeliveryType.COURIER);
        entity.setOrderStatus(OrderStatus.FINISHED);
        entity.setStartDate(new Date());
        entity.setUser(user);
        OrderEntity expectEntity = orderService.save(entity);

        // when
        orderService.delete(expectEntity.getId());

        //then
        assertTrue(orderService.getAll().isEmpty());
        assertNotNull(userService.get(user.getId()));
    }

    @Test
    @Rollback
    public void testFindById() {
        // given
        OrderEntity entity = new OrderEntity();
        entity.setAddress("Город 222");
        entity.setDeliveryType(DeliveryType.COURIER);
        entity.setOrderStatus(OrderStatus.FINISHED);
        entity.setStartDate(new Date());
        entity.setUser(user);
        OrderEntity expectEntity = orderService.save(entity);

        // when
        OrderEntity actualEntity = orderService.get(expectEntity.getId());


        //then
        assertNotNull(actualEntity);
        assertEquals(expectEntity, actualEntity);
    }

    @Test
    @Rollback
    public void testFindAll() {
        // given
        List<OrderEntity> expectEntityList = orderEntityList();
        for (OrderEntity orderEntity:expectEntityList) {
            orderService.save(orderEntity);
        }

        // when
        List<OrderEntity> actualEntityList = orderService.getAll();

        // then
        assertFalse(actualEntityList.isEmpty());
        assertEquals(expectEntityList, expectEntityList);
    }

    private List<OrderEntity> orderEntityList() {
        List<OrderEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderEntity entity = new OrderEntity();
            entity.setAddress("Город 222_" + i);
            entity.setDeliveryType(DeliveryType.COURIER);
            entity.setOrderStatus(OrderStatus.FINISHED);
            entity.setStartDate(new Date());
            entity.setUser(user);
            list.add(entity);
        }
        return list;
    }

    private OrderEntity orderEntity(){
        OrderEntity entity = new OrderEntity();
        entity.setAddress("Улица №002222");
        entity.setDeliveryType(DeliveryType.EXWORKS);
        entity.setOrderStatus(OrderStatus.ACCEPTED);
        entity.setStartDate(new Date());
        entity.setUser(user);
        return entity;
    }
}
