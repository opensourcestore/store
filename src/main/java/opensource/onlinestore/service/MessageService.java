package opensource.onlinestore.service;

import opensource.onlinestore.model.entity.MessageEntity;

import java.util.List;

/**
 * Created by malex on 31.01.16.
 */
public interface MessageService {

    MessageEntity save(MessageEntity entity);

    MessageEntity update(MessageEntity entity);

    void delete(Long id);

    MessageEntity get(Long id);

    List<MessageEntity> getAll();
}
