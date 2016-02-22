package opensource.onlinestore.repository;

import opensource.onlinestore.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by malex on 28.01.16.
 */
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
