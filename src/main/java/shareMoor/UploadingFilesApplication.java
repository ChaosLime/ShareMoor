package shareMoor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import shareMoor.services.StorageProperties;
import shareMoor.services.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

  /**
   * Share Moor
   * 
   * UploadingFilesApplication.java
   * 
   * Purpose: To run and initialize the Spring Boot server. Grabbed this code initially from the
   * following websites. https://spring.io/guides/gs/uploading-files/
   * https://github.com/spring-guides/gs-uploading-files
   * 
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(UploadingFilesApplication.class, args);
  }

  /**
   * Initialize and clean the folders before running the web server.
   * 
   * @param storageService
   * @return
   */
  @Bean
  CommandLineRunner init(StorageService storageService) {
    return (args) -> {
      storageService.deleteAll();
      storageService.init();
    };
  }
}
