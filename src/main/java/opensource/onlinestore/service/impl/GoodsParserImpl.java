package opensource.onlinestore.service.impl;

import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.enums.GoodsRegistryType;
import opensource.onlinestore.service.FileGoodsParser;
import opensource.onlinestore.service.GoodsParser;
import opensource.onlinestore.service.GoodsService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by orbot on 09.02.16.
 */
@Service
@Primary
public class GoodsParserImpl implements GoodsParser {

    private static final Logger LOG = LoggerFactory.getLogger(GoodsParserImpl.class);
    private static final String ARCHIVE_DIR = "archive/";
    private static final String ERRORS_DIR = "errors/";
    private static final String DATE_FORMAT = "dd-MM-yyyy(HH:mm)";
    @Value("${goods.registries.dir}")
    private String registriesDir;
    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    @Qualifier("xlsparser")
    private FileGoodsParser xlsGoodsParser;
    @Autowired
    @Qualifier("csvparser")
    private FileGoodsParser csvParser;
    @Autowired
    private ErrorGoodsStorageBean errorGoodsStorage;

    @Override
    public void parseGoods() {

        try {
            Resource filesResource = new ClassPathResource(registriesDir);
            File[] files;


            files = filesResource.getFile().listFiles();

            if(files == null)
                return;

            Map<GoodsRegistryType, List<File>> filesMap = Arrays.asList(files)
                    .stream()
                    .collect(Collectors.groupingBy(
                            file -> GoodsRegistryType.fromString(file.getName().substring(file.getName().lastIndexOf('.')))
                    ));

            List<File> xlsFiles = filesMap.get(GoodsRegistryType.XLS);
            if(xlsFiles == null) {
                xlsFiles = new ArrayList<>();
            }
            List<File> csvFiles = filesMap.get(GoodsRegistryType.CSV);
            if(csvFiles == null) {
                csvFiles = new ArrayList<>();
            }

            List<GoodsDTO> goods = new ArrayList<>();
            goods.addAll(xlsGoodsParser.parseGoodsFromFiles(xlsFiles));
            goods.addAll(csvParser.parseGoodsFromFiles(csvFiles));

            try {
                goodsService.addListOfGoods(goods);
            } catch (Exception e) {
                LOG.error("Saving goods error", e);
                return;
            }
            List<File> parsedFiles = xlsFiles;
            parsedFiles.addAll(csvFiles);
            moveFilesToArchive(parsedFiles);
            processErrors();
        } catch (IOException e) {
            LOG.error("Read files error", e);
        }
    }

    private void moveFilesToArchive(List<File> files) {
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
            String newFileName = dateFormat.format(new Date()).replace(':', '-') + "_archive_" + file.getName();
            String archiveFilePath = filePath + File.separator + ARCHIVE_DIR;
            new File(archiveFilePath).mkdirs();
            File archiveFile = new File(archiveFilePath + newFileName);
            try {
                FileUtils.moveFile(file, archiveFile);
            } catch (IOException e) {
                LOG.info("Moving file error", e);
            }
        }
    }

    private void processErrors() {
        if(errorGoodsStorage.getErrorGoods().isEmpty()) {
            return;
        }
        List<GoodsDTO> errorRows = errorGoodsStorage.getErrorGoods();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet workSheet = workbook.createSheet("Goods error rows");
        HSSFRow row;
        Iterator<HSSFCell> cellIterator;
        HSSFCell errorCell;
        for(int i = 0; i < errorRows.size(); i++) {
            row = workSheet.createRow(i);
            GoodsDTO goods = errorRows.get(i);
            HSSFRow errorRow = goodsToExcelRow(goods, workSheet, i);
            errorRow.createCell(6).setCellValue(goods.getErrorDescription());
            cellIterator = errorRow.cellIterator();
            int cellCounter = 0;
            while(cellIterator.hasNext()) {
                errorCell = cellIterator.next();
                HSSFCell newErrorCell = row.createCell(cellCounter++);
                newErrorCell.setCellValue(errorCell.toString());
            }
        }
        String errorFileName = dateFormat.format(new Date()).replace(':', '.') + "_errors.xls";
        try {
            File registriesDirFile = new ClassPathResource(registriesDir).getFile();
            File errorDir = new File(registriesDirFile.getAbsolutePath() + File.separator + ERRORS_DIR);
            errorDir.mkdirs();
            FileOutputStream fileOut
                    = new FileOutputStream(errorDir.getAbsolutePath() + File.separator + errorFileName);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
            errorGoodsStorage.clearStorage();
        } catch (IOException e) {
            LOG.error("Could not save error file {}", errorFileName, e);
        }
    }

    private HSSFRow goodsToExcelRow(GoodsDTO goodsDTO, HSSFSheet workSheet, int rowNum) {
        HSSFRow row = workSheet.createRow(rowNum);
        row.createCell(0).setCellValue(goodsDTO.getArticle());
        row.createCell(1).setCellValue(goodsDTO.getName());
        row.createCell(2).setCellValue(goodsDTO.getCount());
        row.createCell(3).setCellValue(goodsDTO.getPrice());
        row.createCell(4).setCellValue(goodsDTO.getProducer());
        row.createCell(5).setCellValue(goodsDTO.getCategoryName());
        return row;
    }
}
