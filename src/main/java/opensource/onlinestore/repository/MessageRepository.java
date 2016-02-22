package opensource.onlinestore.repository;

import opensource.onlinestore.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by malex on 28.01.16.
 */

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
}
