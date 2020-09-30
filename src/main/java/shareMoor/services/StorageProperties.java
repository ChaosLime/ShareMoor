// https://spring.io/guides/gs/uploading-files/
// https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import java.io.File;
import org.springframework.boot.context.properties.ConfigurationProperties;
import shareMoor.domain.ConfigHandler;

@ConfigurationProperties("services")
public class StorageProperties {

  /**
   * Folder location for storing files
   */
  private static String uploadLocation = ConfigHandler.getSettingsDir("upload-dir");
  private static String deniedLocation = ConfigHandler.getSettingsDir("denied-dir");
  private static String reviewThumbLocation = ConfigHandler.getSettingsDir("reviewThumb-dir");
  private static String finishedFullLocation = ConfigHandler.getSettingsDir("processedFull-dir");
  private static String finishedThumbLocation = ConfigHandler.getSettingsDir("processedThumb-dir");
  // The assests Directory should always be in the same location relative to the project.
  private static String assestsLocation = ".." + File.separator + "assests-dir";

  public static String getAssestsLocation() {
    return assestsLocation;
  }

  public static void setAssestsLocation(String assestsLocation) {
    assestsLocation = assestsLocation;
  }

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
