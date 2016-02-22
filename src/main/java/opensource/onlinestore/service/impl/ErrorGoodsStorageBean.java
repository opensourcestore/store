package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.dto.GoodsDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orbot on 06.02.16.
 */
@Component
public class ErrorGoodsStorageBean {
    private List<GoodsDTO> errorDtos;

    public ErrorGoodsStorageBean() {
        errorDtos = new ArrayList<>();
    }

    public List<GoodsDTO> getErrorGoods() {
        return errorDtos;
    }

    public void addErrorGoods(GoodsDTO goods) {
        errorDtos.add(goods);
    }

    public void addErrorGoodsList(List<GoodsDTO> goodsList) {
        errorDtos.addAll(goodsList);
    }

    public void clearStorage() {
        errorDtos.clear();
    }

}
