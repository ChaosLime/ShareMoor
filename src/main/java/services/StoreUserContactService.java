//https://www.w3schools.com/java/java_files_create.asp
package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Service;
import exception.StorageException;

@Service
//@Repository
public class StoreUserContactService {
  final String fileName = "contact_info.csv";
  
  public void writeContactInfo(String contactInfo) {
    createFile(fileName);
    
    try {
      FileWriter myWriter = new FileWriter(fileName);
      myWriter.write("contactInfo" + ",");
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      throw new StorageException("An error occured attempting to add text to file "
          + fileName + ". Error message is: " + e.getMessage());
    }
  }
  
  // Try to create the file. Throw an error if there is an issue.
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
      throw new StorageException("An error occurred attempting to write" + fileName +
          ". Error message is: " + e.getMessage());
    }
  }
}
