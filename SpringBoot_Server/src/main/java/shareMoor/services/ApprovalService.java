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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareMoor.domain.HelperClass;
import shareMoor.exception.StorageException;

/**
 * Share Moor
 * 
 * ApprovalService.java
 * 
 * Purpose: To facilitate the approval or denial of a file that has already been uploaded to Share
 * Moor, but has not been made visible to other users.
 * 
 * @author Mitchell Saunders
 *
 */
@Service
public class ApprovalService {

  private final Path uploadLocation;
  private final Path reviewThumbLocation;
  private final Path finishedThumbLocation;
  private final Path finishedFullLocation;
  private final Path deniedLocation;

  /**
   * Constructor for this service that gathers all folder locations that are defined elsewhere.
   * 
   * @param properties StorageProperties which holds all pertinent information about where files are
   *        stored.
   */
  @Autowired
  public ApprovalService(StorageProperties properties) {
    this.finishedThumbLocation = Paths.get(properties.getFinishedThumbLocation());
    this.finishedFullLocation = Paths.get(properties.getFinishedFullLocation());
    this.deniedLocation = Paths.get(properties.getDeniedLocation());
    this.reviewThumbLocation = Paths.get(properties.getReviewThumbLocation());
    this.uploadLocation = Paths.get(properties.getUploadLocation());
  }

  /**
   * Save file that has been approved into a folder that will make that file visible to the users.
   * 
   * @param fullPath String can be the full file path or the path that is visible to the user on the
   *        web side.
   */
  public void saveFileInFinishedFolder(String fullPath) {
    String cleanedFullPath = HelperClass.cleanOptionalFilePath(fullPath);
    String filename =
        HelperClass.getFilename(cleanedFullPath) + HelperClass.getExtension(cleanedFullPath);

    // Copy main file from review folder to finished folder.
    File source = new File(uploadLocation + File.separator + filename);
    File dest = new File(finishedFullLocation.toString() + File.separator + filename);

    copyFiles(source, dest);
    source.delete();

    // Do the same but for the thumbnail file, which has a different ext and is in a
    // different folder.

    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt =
        HelperClass.findFilenameWOExt(reviewThumbLocation.toString(), filenameWithoutExt);
    source = new File(reviewThumbLocation + File.separator + filenameWithExt);
    dest = new File(finishedThumbLocation + File.separator + filenameWithExt);

    copyFiles(source, dest);
    source.delete();
  }

  public void saveFilesInDeniedFolder(String fullPath) {
    String cleanedFullPath = HelperClass.cleanOptionalFilePath(fullPath);
    String filename =
        HelperClass.getFilename(cleanedFullPath) + HelperClass.getExtension(cleanedFullPath);

    // Copy main file from review folder to finished folder.
    File source = new File(uploadLocation + File.separator + filename);
    File dest = new File(deniedLocation.toString() + File.separator + filename);

    copyFiles(source, dest);
    source.delete();

    // Do the same but for the thumbnail file, which has a different ext and is in a
    // different folder.

    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt =
        HelperClass.findFilenameWOExt(reviewThumbLocation.toString(), filenameWithoutExt);
    source = new File(reviewThumbLocation + File.separator + filenameWithExt);
    // dest = new File(finishedThumbLocation + File.separator + filenameWithExt);

    // copyFiles(source, dest);
    source.delete();
  }

  private void copyFiles(File source, File dest) {
    try {
      Files.copy(source.toPath(), dest.toPath());
    } catch (IOException e) {
      throw new StorageException("Failed to copy over to final folder", e);
    }
  }

}
