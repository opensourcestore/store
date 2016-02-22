package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.entity.AccountEntity;
import opensource.onlinestore.repository.AccountRepository;
import opensource.onlinestore.service.AccountService;
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
public class AccountServiceImpl implements AccountService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository repository;

    @Autowired
    private Mapper beanMapper;

    @Override
    public AccountEntity save(AccountEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public AccountEntity update(AccountEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public AccountEntity get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<AccountEntity> getAll() {
        return repository.findAll();
    }
}
