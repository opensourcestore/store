package opensource.onlinestore.service;

import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.model.entity.GoodsEntity;

import java.util.List;

public interface GoodsService {

    boolean isCharacteristicsValid(GoodsEntity goods);

    boolean isCharacteristicsValid(GoodsDTO goods);

    boolean addGoods(GoodsDTO goodsDTO);

    boolean addListOfGoods(List<GoodsDTO> goodsList);

    List<GoodsDTO> getGoods(CategoryEntity category);

    GoodsEntity save(GoodsEntity entity);

    GoodsEntity update(GoodsEntity entity);

    void delete(Long id);

    GoodsEntity get(Long id);

    List<GoodsEntity> getAll();

    GoodsDTO convertEntityToDTO(GoodsEntity goodsEntity);

    GoodsEntity convertDTOToEntity(GoodsDTO goodsDTO);
}
