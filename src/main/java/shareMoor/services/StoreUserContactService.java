// https://www.w3schools.com/java/java_files_create.asp
package shareMoor.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Service;
import shareMoor.exception.StorageException;

@Service
public class StoreUserContactService {
  // final String fileName = ConfigService.getSettingsDir("contactInfo-dir");
  final String fileName = ConfigService.getSettingsDir("contactInfo-name");
  final String filePath = ConfigService.getSettingsDir("contactInfo-dir") + fileName;

  public void writeContactInfo(String contactInfo) {
    createFile(filePath);

    try {
      FileWriter myWriter = new FileWriter(filePath, true);
      myWriter.write(contactInfo + System.getProperty("line.separator"));
      myWriter.close();
      System.out.println("Successfully wrote " + contactInfo + " to the file.");
    } catch (IOException e) {
      throw new StorageException("An error occured attempting to add text to file " + fileName
          + ". Error message is: " + e.getMessage());
    }
  }

  // Try to create the file. The row an error if there is an issue.
  private void createFile(String fileName) {
    try {
      File myObj = new File("contact_info.csv");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      throw new StorageException("An error occurred attempting to write" + fileName
          + ". Error message is: " + e.getMessage());
    }
  }
}
