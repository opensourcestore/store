package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.dto.PaymentResponseDTO;
import opensource.onlinestore.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    //todo: make an implementation with some mock payment processing
    @Override
    public PaymentResponseDTO pay(List<GoodsDTO> goods) {
        return null;
    }
}
