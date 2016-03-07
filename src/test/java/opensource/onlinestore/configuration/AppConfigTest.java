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
        ClassPathResource local = new ClassPathResource("hsqldb.properties");
       /* ClassPathResource local = new ClassPathResource("local.properties");*/
        configurer.setLocation(local);
        /*ClassPathResource application = new ClassPathResource("application.properties");
        if (local.exists()) {
            configurer.setLocations(application, local);
        } else {
            configurer.setLocation(application);
        }*/
        return configurer;
    }


    @Bean
    public Mapper beanMapper() {
        return new DozerBeanMapper();
    }
}
