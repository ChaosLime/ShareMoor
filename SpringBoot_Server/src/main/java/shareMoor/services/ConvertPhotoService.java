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
package shareMoor.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareMoor.domain.HelperClass;
import shareMoor.exception.StorageException;

@Service
public class ConvertPhotoService {
  // private int imageCounter = 1;
  // This service may not be used at all.

  private final Path needsReviewLocation;

  @Autowired
  public ConvertPhotoService(StorageProperties properties) {
    this.needsReviewLocation = Paths.get(properties.getDeniedLocation());
  }

  public String convertPhoto(String filePath) {

    BufferedImage img = null;

    String newFileExtension = ".tiff";

    String newFilePath =
        needsReviewLocation + File.separator + HelperClass.getFilename(filePath) + newFileExtension;

    final ImageFormat desiredImageFormat = ImageFormats.PNG;

    // String desiredFilename = "../upload-dir/" + String.valueOf(imageCounter) + ".tiff";

    // First read file into BufferedImage obj.
    try {
      img = Imaging.getBufferedImage(new File(filePath));
    } catch (IOException | ImageReadException e) {
      System.out.println("Failed to read file or photo. Error message: " + e.getMessage());
    }

    // String filenameWithoutExt = filePath.replaceFirst("[.][^.]+$", "");

    try {
      // TODO: remove hardcoded destination from here.
      Imaging.writeImage(img, new File(newFilePath), desiredImageFormat, null);
    } catch (IOException | ImageWriteException e) {
      throw new StorageException("Unable to save photo with new format.", e);
    }

    File f = new File(newFilePath);
    if (f.exists() && !f.isDirectory()) {
      return newFilePath;
    } else {
      return newFilePath;
    }
  }

  public boolean deleteOriginal(String filePath) {
    // TODO: Delete the original file that is now out of date.
    return true;
  }
}
