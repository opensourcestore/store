package opensource.onlinestore.model.util;

import opensource.onlinestore.model.entity.GoodsEntity;

/**
 * Created by malex on 29.01.16.
 */
public class GoodsEntityUtil {

    public static GoodsEntity createGoodsEntity(){
        GoodsEntity entity =  new GoodsEntity();
        entity.setId(1L);
        entity.setName("Nokia 3110");
        entity.setArticle("Телефон");
        entity.setCounts(1L);
        entity.setCategory(null);
        entity.setPrice(99.01);
        entity.setProducer("Китай");
        return entity;
    }
}
