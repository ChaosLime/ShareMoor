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

@Service
public class ApprovalService {
  
  private final Path reviewThumbLocation;
  private final Path finishedThumbLocation;
  private final Path finishedFullLocation;
  private final Path deniedLocation;
  
  @Autowired
  public ApprovalService(StorageProperties properties) {
    this.finishedThumbLocation = Paths.get(properties.getFinishedThumbLocation());
    this.finishedFullLocation = Paths.get(properties.getFinishedFullLocation());
    this.deniedLocation = Paths.get(properties.getDeniedLocation());
    this.reviewThumbLocation = Paths.get(properties.getReviewThumbLocation());
  }
  
  public void saveFileInFinishedFolder(String fullPath) {
    String filename = HelperClass.getFilename(fullPath) +
                      HelperClass.getExtension(fullPath);
    
    // Copy main file from review folder to finished folder.
    File source = new File(fullPath);
    File dest = new File(finishedFullLocation.toString() + File.separator + filename);
    
    copyFiles(source, dest);
    source.delete();
    
    // Do the same but for the thumbnail file, which has a different ext and is in a
    // different folder.
    
    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt = HelperClass.findFilenameWOExt(reviewThumbLocation.toString(),
                                                          filenameWithoutExt);
    source = new File(reviewThumbLocation + File.separator + filenameWithExt);
    dest = new File(finishedThumbLocation + File.separator + filenameWithExt);
    
    copyFiles(source, dest);
    source.delete();
  }
  
  public void saveFilesInDeniedFolder(String fullPath) {
    // TODO: Flesh this out. Mirror the method above but to a the denied folder.
  }
  
  private void copyFiles(File source, File dest) {
    try {
      Files.copy(source.toPath(), dest.toPath());
    } catch (IOException e) {
      throw new StorageException("Failed to copy over to final folder", e);
    }
  }

}
