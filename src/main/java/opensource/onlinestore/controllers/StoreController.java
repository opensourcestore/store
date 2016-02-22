package opensource.onlinestore.controllers;

import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.dto.PaymentResponseDTO;
import opensource.onlinestore.service.GoodsService;
import opensource.onlinestore.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class StoreController {

    private static final Logger LOG = LoggerFactory.getLogger(StoreController.class);
    private static final String REGISTRIES_DIR = "goodsregistries/";
    private static final String DATE_FORMAT = "dd-MM-yyyy(HH:mm)";
    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "main";
    }

    //todo: Secure with hasRole("ADMIN") when security configured
    @RequestMapping(value = "/admin/addgoods", method = RequestMethod.POST)
    @ResponseBody
    public boolean addGoods(GoodsDTO goods) {
        return goodsService.addGoods(goods);
    }

//    @RequestMapping(value = "/getgoods", method = RequestMethod.GET)
//    @ResponseBody
//    public List<GoodsDTO> getGoods(Category category) {
//        return goodsService.getGoods(category);
//    }

    //todo: Secure with hasRole("USER") when security configured
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    @ResponseBody
    public PaymentResponseDTO payment(List<GoodsDTO> goods) {
        return paymentService.pay(goods);
    }

    @RequestMapping(value = "/uploadregistries", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadGoodsRegistries(@RequestParam("file") List<MultipartFile> files) throws IOException {
        Resource filesResource = new ClassPathResource(REGISTRIES_DIR);
        File registriesDir = filesResource.getFile();
        String fileName = dateFormat.format(new Date()) + "_goods_registry";
        for(int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            File destFile = new File(registriesDir + File.separator + i + fileName
                    + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.'))
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
            file.transferTo(destFile);
        }
    }

}
