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
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shareMoor.domain.CrossPlatformTools;

/**
 * Use of the tool Exiftool by Phil Harvey is a game changer. https://exiftool.org/ All
 * documentation of uses are on his FAQ and forums.
 * 
 * @author nick
 *
 */
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
