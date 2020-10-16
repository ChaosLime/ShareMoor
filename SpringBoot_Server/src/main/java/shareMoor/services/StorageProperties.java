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
  // The assets Directory should always be in the same location relative to the project.
  private static String assetsLocation = ".." + File.separator + "assets-dir" + File.separator;

  public static String getAssetsLocation() {
    return assetsLocation;
  }

  public static void setAssetsLocation(String assetsLocation) {
    StorageProperties.assetsLocation = assetsLocation;
  }

  public String getUploadLocation() {
    return uploadLocation;
  }

  public void setUploadLocation(String uploadLocation) {
    StorageProperties.uploadLocation = uploadLocation;
  }

  public String getDeniedLocation() {
    return deniedLocation;
  }

  public void setDeniedLocation(String deniedLocation) {
    StorageProperties.deniedLocation = deniedLocation;
  }

  public String getReviewThumbLocation() {
    return reviewThumbLocation;
  }

  public void setReviewThumbLocation(String reviewThumbLocation) {
    StorageProperties.reviewThumbLocation = reviewThumbLocation;
  }

  public String getFinishedFullLocation() {
    return finishedFullLocation;
  }

  public void setFinishedFullLocation(String finishedFullLocation) {
    StorageProperties.finishedFullLocation = finishedFullLocation;
  }

  public String getFinishedThumbLocation() {
    return finishedThumbLocation;
  }

  public void setFinishedThumbLocation(String finishedThumbLocation) {
    StorageProperties.finishedThumbLocation = finishedThumbLocation;
  }
}
