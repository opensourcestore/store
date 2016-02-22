package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.repository.CategoryRepository;
import opensource.onlinestore.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository repository;


    @Autowired
    private Mapper beanMapper;

    @Override
    public CategoryEntity save(CategoryEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public CategoryEntity get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<CategoryEntity> getAll() {
        return repository.findAll();
    }
}
