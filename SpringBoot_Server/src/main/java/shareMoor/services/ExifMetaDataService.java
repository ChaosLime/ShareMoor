package shareMoor.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareMoor.domain.CrossPlatformTools;

@Service
public class ExifMetaDataService {

  private static Path assestDir;

  @Autowired
  public ExifMetaDataService(StorageProperties properties) {
    Paths.get(properties.getDeniedLocation());
    ExifMetaDataService.assestDir = Paths.get(StorageProperties.getAssetsLocation());
  }

  /**
   * Much like the name, this program scrubs files based on location using Exiftool. The program
   * quietly removes all metadata on the file while adding a keyword tag, and preserving the
   * orientation of the image if it exists.
   * 
   * @param storedFileLocation
   * @param status
   */
  public void scrubFile(String storedFileLocation, String status) {
    String assestPathStr = assestDir.toString() + File.separator;

    String cmd = "";
    String program = CrossPlatformTools.setUpExifToolCall(assestPathStr);

    cmd = program + " -q -all:all= -Keywords=" + status + " -tagsfromfile @ -orientation "
        + storedFileLocation;
    CrossPlatformTools.callSystemProgram(cmd);

    File file = new File(storedFileLocation + "_original");
    file.delete();
  }

}
