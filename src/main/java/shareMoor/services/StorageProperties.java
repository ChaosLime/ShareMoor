//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import java.io.File;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("services")
public class StorageProperties {

  /**
   * Folder location for storing files
   */
  
  // TODO: Grab these locations from the config file that will sit outside of the JAR
  private String uploadLocation = ".." + File.separator + "upload-dir";
  private String deniedLocation = ".." + File.separator + "denied-dir";
  private String reviewThumbLocation = ".." + File.separator + "reviewThumb-dir";
  private String finishedFullLocation = ".." + File.separator + "processedFull-dir";
  private String finishedThumbLocation = ".." + File.separator + "processedThumb-dir";
  
  public String getUploadLocation() {
    return uploadLocation;
  }
  public void setUploadLocation(String uploadLocation) {
    this.uploadLocation = uploadLocation;
  }
  public String getDeniedLocation() {
    return deniedLocation;
  }
  public void setDeniedLocation(String deniedLocation) {
    this.deniedLocation = deniedLocation;
  }
  public String getReviewThumbLocation() {
    return reviewThumbLocation;
  }
  public void setReviewThumbLocation(String reviewThumbLocation) {
    this.reviewThumbLocation = reviewThumbLocation;
  }
  public String getFinishedFullLocation() {
    return finishedFullLocation;
  }
  public void setFinishedFullLocation(String finishedFullLocation) {
    this.finishedFullLocation = finishedFullLocation;
  }
  public String getFinishedThumbLocation() {
    return finishedThumbLocation;
  }
  public void setFinishedThumbLocation(String finishedThumbLocation) {
    this.finishedThumbLocation = finishedThumbLocation;
  }
}