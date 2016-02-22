package opensource.onlinestore.model.util;

import opensource.onlinestore.model.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malex on 01.02.16.
 */
public class CategoryEntityUtil {

    public static CategoryEntity createCategory() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName("Бытовая техника");
        List<String> characteristicKeys = new ArrayList<>();
        characteristicKeys.add("age");
        entity.setCharacteristicsTemplateFromList(characteristicKeys);
        return entity;
    }

    public static List<CategoryEntity> createCategoryEntityList() {
        List<CategoryEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CategoryEntity entity = new CategoryEntity();
            entity.setName("Бытовая техника_" + i);
            List<String> characteristicKeys = new ArrayList<>();
            characteristicKeys.add("age");
            entity.setCharacteristicsTemplateFromList(characteristicKeys);
            list.add(entity);
        }
        return list;
    }
}
