package opensource.onlinestore.service;

import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.dto.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO pay(List<GoodsDTO> goods);
}
