package opensource.onlinestore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by orbot on 04.02.16.
 */
@Service
public class StoreScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(StoreScheduler.class);

    @Autowired
    private GoodsParser goodsParser;

    @Scheduled(fixedDelay = 1000L * 60 * 60)
    public void launchXlsParser() {
        LOG.info("Launching goodsParser");
        goodsParser.parseGoods();
    }

}
