// https://spring.io/guides/gs/uploading-files/
// https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("services")
public class StorageProperties {

  /**
   * Folder location for storing files
   */
  private static String uploadLocation = ConfigService.getSettingsDir("upload-dir");
  private static String deniedLocation = ConfigService.getSettingsDir("denied-dir");
  private static String reviewThumbLocation = ConfigService.getSettingsDir("reviewThumb-dir");
  private static String finishedFullLocation = ConfigService.getSettingsDir("processedFull-dir");
  private static String finishedThumbLocation = ConfigService.getSettingsDir("processedThumb-dir");

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
