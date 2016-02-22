package opensource.onlinestore.service;

import opensource.onlinestore.model.entity.CategoryEntity;

import java.util.List;

/**
 * Created by malex on 31.01.16.
 */
public interface CategoryService {

    CategoryEntity save(CategoryEntity entity);

    CategoryEntity update(CategoryEntity entity);

    void delete(Long id);

    CategoryEntity get(Long id);

    List<CategoryEntity> getAll();
}
