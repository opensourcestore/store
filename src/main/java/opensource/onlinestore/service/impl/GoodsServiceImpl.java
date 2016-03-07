package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.model.entity.GoodsEntity;
import opensource.onlinestore.repository.CategoryRepository;
import opensource.onlinestore.repository.GoodsRepository;
import opensource.onlinestore.service.GoodsService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger LOG = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Mapper beanMapper;
    @Autowired
    ErrorGoodsStorageBean errorGoodsStorage;

    @Override
    public boolean isCharacteristicsValid(GoodsEntity goods) {
        return isCharacteristicsValid(convertEntityToDTO(goods));
    }

    @Override
    public boolean isCharacteristicsValid(GoodsDTO goods) {
        if(goods.getCharacteristicsMap() == null) {
            return true;
        }
        boolean valid = true;
        CategoryEntity category = categoryRepository.findByName(goods.getCategoryName());
        List<String> characteristicKeys = category.getCharacteristicsKeys();
        Map<String, String> characteristics = goods.getCharacteristicsMap();
        for(String key : characteristics.keySet()) {
            if(!characteristicKeys.contains(key)) {
                valid = false;
            }
        }
        return valid;
    }

    @Override
    @Transactional
    public boolean addGoods(GoodsDTO goodsDTO) {
        GoodsEntity goodsEntity = beanMapper.map(goodsDTO, GoodsEntity.class);
        CategoryEntity category = categoryRepository.findByName(goodsDTO.getCategoryName());
        goodsEntity.setCategory(category);
        save(goodsEntity);
        return true;
    }

    @Override
    @Transactional
    public boolean addListOfGoods(List<GoodsDTO> goodsList) {
        boolean success = true;
        for(GoodsDTO goods : goodsList) {
            GoodsEntity retrievedGoods = goodsRepository.findByArticle(goods.getArticle());

            if(retrievedGoods != null) {
                GoodsEntity goodsFromListEntity = convertDTOToEntity(goods);
                if(!goodsFromListEntity.equals(retrievedGoods)) {
                    String errorMessage = "Unable to save different goods with same article";
                    goods.setErrorDescription(errorMessage);
                    LOG.error(errorMessage + retrievedGoods.getArticle());
                    errorGoodsStorage.addErrorGoods(goods);
                    return false;
                }
                retrievedGoods.setCounts(goods.getCounts() + retrievedGoods.getCounts());
                goodsRepository.save(retrievedGoods);
            } else {
                success = success && addGoods(goods);
            }
        }
        return true;
    }

    @Override
    public List<GoodsDTO> getGoods(CategoryEntity category) {
        return null;
    }

    @Override
    public GoodsEntity save(GoodsEntity entity) {
        if(!isCharacteristicsValid(entity)) {
            throw new ConstraintViolationException("Characteristics not valid", null);
        }
        return goodsRepository.saveAndFlush(entity);
    }

    @Override
    public GoodsEntity update(GoodsEntity entity) {
        return goodsRepository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        goodsRepository.delete(id);
    }

    @Override
    public GoodsEntity get(Long id) {
        return goodsRepository.findOne(id);
    }

    @Override
    public List<GoodsEntity> getAll() {
        return goodsRepository.findAll();
    }

    @Override
    public GoodsDTO convertEntityToDTO(GoodsEntity goodsEntity) {
        GoodsDTO goodsDTO = beanMapper.map(goodsEntity, GoodsDTO.class);
        goodsDTO.setCategoryName(goodsEntity.getCategory().getName());
        goodsDTO.setCharacteristicsMap(goodsEntity.getCharachteristicsAsMap());
        return goodsDTO;
    }

    @Override
    public GoodsEntity convertDTOToEntity(GoodsDTO goodsDTO) {
        GoodsEntity goodsEntity = beanMapper.map(goodsDTO, GoodsEntity.class);
        CategoryEntity category = categoryRepository.findByName(goodsDTO.getCategoryName());
        goodsEntity.setCategory(category);
        goodsEntity.setCharachteristicsFromMap(goodsDTO.getCharacteristicsMap());
        return goodsEntity;
    }

}
