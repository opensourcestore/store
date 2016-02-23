package opensource.onlinestore.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@ComponentScan(basePackages = "opensource.onlinestore")
public class AppConfig {


    @Bean
    public PropertyPlaceholderConfigurer propertyConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();

        ClassPathResource local = new ClassPathResource("local.properties");
        ClassPathResource application = new ClassPathResource("application.properties");
        if (local.exists()) {
            configurer.setLocations(application, local);
        } else {
            configurer.setLocation(application);
        }
        return configurer;
    }

    @Bean
    public Mapper beanMapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
