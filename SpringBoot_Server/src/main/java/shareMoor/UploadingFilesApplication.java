/*
 * This file is part of Share Moor
 * 
 * Share Moor is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Share Moor is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Share Moor. If not,
 * see <https://www.gnu.org/licenses/>.
 */
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
