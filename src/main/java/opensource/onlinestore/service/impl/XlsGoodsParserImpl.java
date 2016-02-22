package opensource.onlinestore.service.impl;

import opensource.onlinestore.Utils.Exceptions.NoCategoryException;
import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.model.enums.GoodsRegistryType;
import opensource.onlinestore.repository.CategoryRepository;
import opensource.onlinestore.service.FileGoodsParser;
import opensource.onlinestore.service.GoodsService;
import opensource.onlinestore.service.GoodsParser;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by orbot on 04.02.16.
 */
@Service("xlsparser")
public class XlsGoodsParserImpl implements FileGoodsParser {

    private static final Logger LOG = LoggerFactory.getLogger(XlsGoodsParserImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ErrorGoodsStorageBean errorGoodsStorage;

    @Override
    public List<GoodsDTO> parseGoodsFromFiles(List<File> files) {

        if(files == null)
            return null;
        LOG.info("Launching xls goods parser");
        LOG.info("Found {} xls files", files.size());
        List<GoodsDTO> goods = files
                .stream()
                .map(this::parseFile)
                .flatMap(List::stream)
                .filter(filesList -> filesList != null)
                .collect(Collectors.toList());
        return goods;
    }

    private List<GoodsDTO> parseFile(File file) {
        try {
            LOG.info("Starting parsing goods from file {}", file.getName());
            List<GoodsDTO> goodsList = new ArrayList<>();
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet hssfSheet = workbook.getSheetAt(0);
            Iterator rowIterator = hssfSheet.rowIterator();
            while (rowIterator.hasNext()) {
                HSSFRow row = (HSSFRow)rowIterator.next();
                GoodsDTO parsedGoods = parseRow(row);
                if(parsedGoods != null) {
                    goodsList.add(parsedGoods);
                }
            }
            return goodsList;
        } catch (IOException e) {
            LOG.error("Could not parse file {}", file.getName(), e);
            return null;
        }
    }

    private GoodsDTO parseRow(HSSFRow row) {
        try {
            GoodsDTO goods = new GoodsDTO();
            goods.setArticle(row.getCell(0).toString());
            goods.setName(row.getCell(1).toString());
            Double countDouble = Double.parseDouble(row.getCell(2).toString());
            goods.setCount(countDouble.longValue());
            goods.setPrice(Double.parseDouble(row.getCell(3).toString()));
            goods.setProducer(row.getCell(4).toString());
            String categoryName = row.getCell(5).toString();
            goods.setCategoryName(categoryName);
            CategoryEntity category = categoryRepository.findByName(categoryName);
            if(category == null) {
                String errorMessage = "No category " + categoryName + " found";
                goods.setErrorDescription(errorMessage);
                errorGoodsStorage.addErrorGoods(goods);
                throw new NoCategoryException(errorMessage);
            }
            return goods;
        } catch (Exception e) {
            LOG.error("Could not parse goods from row", e);
            return null;
        }
    }


}
