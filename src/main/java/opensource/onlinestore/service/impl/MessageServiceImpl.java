package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.entity.MessageEntity;
import opensource.onlinestore.repository.MessageRepository;
import opensource.onlinestore.service.MessageService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by malex on 31.01.16.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageRepository repository;

    @Autowired
    private Mapper beanMapper;

    @Override
    public MessageEntity save(MessageEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public MessageEntity update(MessageEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public MessageEntity get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<MessageEntity> getAll() {
        return repository.findAll();
    }
}
