//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("services")
public class StorageProperties {

  /**
   * Folder location for storing files
   */
  private String location = "../upload-dir";

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}