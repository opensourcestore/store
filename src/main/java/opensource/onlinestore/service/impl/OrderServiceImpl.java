package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.entity.OrderEntity;
import opensource.onlinestore.repository.OrderRepository;
import opensource.onlinestore.service.OrderService;
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
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository repository;

    @Autowired
    private Mapper beanMapper;

    @Override
    public OrderEntity save(OrderEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public OrderEntity update(OrderEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public OrderEntity get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<OrderEntity> getAll() {
        return repository.findAll();
    }
}
