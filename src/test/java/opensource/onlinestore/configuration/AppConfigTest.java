package opensource.onlinestore.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by orbot on 29.01.16.
 */
@Profile("test")
@Configuration
@Import(JPAConfigTest.class)
@ComponentScan(basePackages = {"opensource.onlinestore.service"})
public class AppConfigTest {

    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties"));
        return configurer;
    }


    @Bean
    public Mapper beanMapper() {
        return new DozerBeanMapper();
    }
}
