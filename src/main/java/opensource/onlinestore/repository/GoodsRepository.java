package opensource.onlinestore.repository;

import opensource.onlinestore.model.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by orbot on 14.01.16.
 */
public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
    GoodsEntity findByArticle(String article);
}
