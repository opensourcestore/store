package opensource.onlinestore.service;

import opensource.onlinestore.model.entity.OrderEntity;

import java.util.List;

/**
 * Created by malex on 31.01.16.
 */
public interface OrderService {
    OrderEntity save(OrderEntity entity);

    OrderEntity update(OrderEntity entity);

    void delete(Long id);

    OrderEntity get(Long id);

    List<OrderEntity> getAll();

}
