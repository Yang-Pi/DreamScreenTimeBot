package mvc;

import mvc.amazon.AmazonS3ClientService;
import mvc.controller.ImageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class SpringConfig {
    @Bean
    public AmazonS3ClientService amazonS3ClientService() {
        return new AmazonS3ClientService();
    }
}
