package shareMoor.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareMoor.domain.CrossPlatformTools;

@Service
public class exifMetaDataService {
  /*
   * 
   * 
   */
  private final Path needsReviewLocation;
  private static Path assestDir;

  @Autowired
  public exifMetaDataService(StorageProperties properties) {
    this.needsReviewLocation = Paths.get(properties.getDeniedLocation());
    this.assestDir = Paths.get(properties.getAssestsLocation());
  }

  public static void scrubFile(String storedFileLocation, String status) {
    System.out.println("Scrubbing metadata.");
    String assestPathStr = assestDir.toString();
    CrossPlatformTools.setUpExifToolCall(assestPathStr, storedFileLocation, status);
  }

}
