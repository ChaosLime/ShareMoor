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

// https://www.w3schools.com/java/java_files_create.asp
package shareMoor.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.stereotype.Service;
import shareMoor.domain.ConfigHandler;
import shareMoor.exception.StorageException;

@Service
public class StoreUserContactService {
  // final String fileName = ConfigService.getSettingsDir("contactInfo-dir");
  final String fileName = ConfigHandler.getSettingsDir("contactInfo-name");
  final String filePath = ConfigHandler.getSettingsDir("contactInfo-dir") + fileName;

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
