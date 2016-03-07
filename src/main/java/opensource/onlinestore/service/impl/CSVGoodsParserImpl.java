package opensource.onlinestore.service.impl;

import au.com.bytecode.opencsv.CSVReader;
import opensource.onlinestore.Utils.Exceptions.NoCategoryException;
import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.entity.CategoryEntity;
import opensource.onlinestore.repository.CategoryRepository;
import opensource.onlinestore.service.FileGoodsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by orbot on 09.02.16.
 */
@Service("csvparser")
public class CSVGoodsParserImpl implements FileGoodsParser {

    private static final Logger LOG = LoggerFactory.getLogger(CSVGoodsParserImpl.class);

    @Autowired
    private ErrorGoodsStorageBean errorGoodsStorage;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<GoodsDTO> parseGoodsFromFiles(List<File> files) {
        if(files == null)
            return null;
        LOG.info("Launching csv goods parser");
        LOG.info("Found {} csv files", files.size());
        List<GoodsDTO> goods = files
                .stream()
                .map(this::parseFile)
                .flatMap(List::stream)
                .filter(filesList -> filesList != null)
                .collect(Collectors.toList());
        return goods;
    }

    private List<GoodsDTO> parseFile(File file) {
        LOG.info("Starting parsing goods from file {}", file.getName());

        try {
            CSVReader reader = new CSVReader(new FileReader(file), ',');
            List<GoodsDTO> goodsList = new ArrayList<>();
            String[] record;

            while((record = reader.readNext()) != null) {
                GoodsDTO goodsFromRow = parseRow(record);
                if(goodsFromRow != null) {
                    goodsList.add(goodsFromRow);
                }
            }
            return goodsList;
        } catch (IOException e) {
            LOG.info("Could not parse file {}", file.getName());
            return null;
        }
    }

    private GoodsDTO parseRow(String[] row) {
        try {
            List<String> rowList = Arrays.asList(row);
            rowList = rowList.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
            GoodsDTO goods = new GoodsDTO();
            goods.setArticle(rowList.get(0));
            goods.setName(rowList.get(1));
            goods.setCounts(Long.parseLong(rowList.get(2)));
            goods.setPrice(Double.parseDouble(rowList.get(3)));
            goods.setProducer(rowList.get(4));
            String categoryName = rowList.get(5);
            goods.setCategoryName(categoryName);
            CategoryEntity category = categoryRepository.findByName(categoryName);
            if (category == null) {
                String errorMessage = "No category " + goods.getCategoryName() + " found";
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
