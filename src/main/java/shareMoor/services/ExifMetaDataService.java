package shareMoor.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareMoor.domain.CrossPlatformTools;

@Service
public class ExifMetaDataService {

  private final Path needsReviewLocation;
  private static Path assestDir;

  @Autowired
  public ExifMetaDataService(StorageProperties properties) {
    this.needsReviewLocation = Paths.get(properties.getDeniedLocation());
    this.assestDir = Paths.get(properties.getAssestsLocation());
  }

  public void scrubFile(String storedFileLocation, String status) {
    String assestPathStr = assestDir.toString() + File.separator;

    String cmd = "";
    String program = CrossPlatformTools.setUpExifToolCall(assestPathStr);
    
    //String now = getDateTime();

    cmd = program + " -q -all:all= -Keywords=" + status + " -tagsfromfile @ -orientation "
        + storedFileLocation;
    CrossPlatformTools.callSystemProgram(cmd);

    File file = new File(storedFileLocation + "_original");
    file.delete();
    

  }

 

}
