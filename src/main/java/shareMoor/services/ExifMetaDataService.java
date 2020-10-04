package shareMoor.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
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

  public static void scrubFile(String storedFileLocation, String status) {
    System.out.println("Scrubbing metadata.");
    String assestPathStr = assestDir.toString();

    String cmd = "";
    String program = CrossPlatformTools.setUpExifToolCall(assestPathStr);

    cmd = program + " -b -createdate " + storedFileLocation;
    String createDate = CrossPlatformTools.callSystemProgram(cmd);

    cmd = program + " -q -all= " + storedFileLocation;
    CrossPlatformTools.callSystemProgram(cmd);

    createDate = checkCreateDate(createDate);

    cmd = program + " -createdate=" + createDate + " " + storedFileLocation;
    CrossPlatformTools.callSystemProgram(cmd);

    cmd = program + " -q -Keywords=" + status + " " + storedFileLocation;
    CrossPlatformTools.callSystemProgram(cmd);

    cmd = program + " -q -delete_original! " + storedFileLocation;
    CrossPlatformTools.callSystemProgram(cmd);
  }

  private static String checkCreateDate(String createDate) {
    if (createDate.equals("")) {
      System.out.println("No create date found. Grabbing system time.");
      Date date = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
      createDate = formatter.format(date).toString();
    }
    createDate = createDate.replace(" ", "\b");
    return createDate;

  }

}
