package opensource.onlinestore.service.impl;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import opensource.onlinestore.configuration.AppConfigTest;
import opensource.onlinestore.model.dto.GoodsDTO;
import opensource.onlinestore.model.entity.GoodsEntity;
import opensource.onlinestore.repository.GoodsRepository;
import opensource.onlinestore.service.GoodsParser;
import opensource.onlinestore.service.GoodsService;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by orbot on 04.02.16.
 */
// TODO: 22.02.2016 НЕ РАБОТАЕТ!!!!!

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfigTest.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class GoodsParserImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String REGISTRIES_DIR = "goodsregistries/";
    private static final String ARCHIVE_DIR = "archive/";
    private static final String ERRORS_DIR = "errors/";
    private static final String TEMPLATE_EXTENSION = ".tmpl";

    @Autowired
    private GoodsParser goodsParser;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsService goodsService;

    @BeforeClass
    public static void prepareTestFiles() throws IOException {
        Resource filesResource = new ClassPathResource(REGISTRIES_DIR);
        File[] files;

        files = filesResource.getFile().listFiles((dir, name) -> {
            return name.endsWith(TEMPLATE_EXTENSION);
        });

        for(File file : files) {
            String absolutePath = file.getAbsolutePath();
            FileUtils.copyFile(file, new File(absolutePath.substring(0, absolutePath.lastIndexOf("."))));
        }
    }

    @AfterClass
    public static void removeArchiveAndErrors() throws IOException {
        File registriesDir = new ClassPathResource(REGISTRIES_DIR).getFile();
        File archiveDir = new File(registriesDir.getAbsolutePath() + File.separator + ARCHIVE_DIR);
        File errorsDir = new File(registriesDir.getAbsolutePath() + File.separator + ERRORS_DIR);
        FileUtils.deleteDirectory(archiveDir);
        FileUtils.deleteDirectory(errorsDir);
    }

    @Test
    @Rollback
    @DatabaseSetup(value = {"classpath:categories.xml"})
    public void testAParseFiles() {
        goodsParser.parseGoods();
        GoodsEntity goodsEntity1 = goodsRepository.findByArticle("Test-article-1");
        GoodsEntity goodsEntity2 = goodsRepository.findByArticle("Test-article-2");
        GoodsEntity goodsEntity3 = goodsRepository.findByArticle("Test-article-3");
        GoodsDTO goodsExpectedDTO1 = new GoodsDTO();
        goodsExpectedDTO1.setArticle("Test-article-1");
        goodsExpectedDTO1.setCount(10L);
        goodsExpectedDTO1.setName("Здоровая лопата");
        goodsExpectedDTO1.setPrice(3D);
        goodsExpectedDTO1.setProducer("Samsung");
        goodsExpectedDTO1.setCategoryName("Телефоны");
        GoodsEntity goodsExpectedEntity1 = goodsService.convertDTOToEntity(goodsExpectedDTO1);
        Assert.assertEquals(goodsExpectedEntity1, goodsEntity1);

        GoodsDTO goodsExpectedDTO2 = new GoodsDTO();
        goodsExpectedDTO2.setArticle("Test-article-2");
        goodsExpectedDTO2.setCount(2L);
        goodsExpectedDTO2.setName("Яблочко");
        goodsExpectedDTO2.setPrice(666D);
        goodsExpectedDTO2.setProducer("Apple");
        goodsExpectedDTO2.setCategoryName("Планшеты");
        GoodsEntity goodsExpectedEntity2 = goodsService.convertDTOToEntity(goodsExpectedDTO2);
        Assert.assertEquals(goodsExpectedEntity2, goodsEntity2);

        Assert.assertNull(goodsEntity3);
    }

    @Test
    public void testCheckArchiveFileNames() throws IOException {
        File registriesDir = new ClassPathResource(REGISTRIES_DIR).getFile();
        File archiveDir = new File(registriesDir.getAbsolutePath() + File.separator + ARCHIVE_DIR);
        File[] files = archiveDir.listFiles();
        Assert.assertNotNull(files);
        for(File file : files) {
            String fileName = file.getName();
            Assert.assertTrue(fileName.matches("\\d{2}-\\d{2}-\\d{4}\\(\\d{2}:\\d{2}\\)_archive_test\\d*.(xls|csv)"));
        }
    }

    @Test
    public void testCheckErrorFileNames() throws IOException {
        File registriesDir = new ClassPathResource(REGISTRIES_DIR).getFile();
        File archiveDir = new File(registriesDir.getAbsolutePath() + File.separator + ERRORS_DIR);
        File[] files = archiveDir.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(files.length, 1);
        Assert.assertTrue(files[0].getName().matches("\\d{2}-\\d{2}-\\d{4}\\(\\d{2}:\\d{2}\\)_errors.xls"));
    }
}