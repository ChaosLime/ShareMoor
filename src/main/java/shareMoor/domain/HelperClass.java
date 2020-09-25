package shareMoor.domain;

import java.io.File;

public class HelperClass {

  public static String getExtension(String fullPath) {
    int dot = fullPath.lastIndexOf(".");
    return fullPath.substring(dot);
  }

  public static String getFilename(String fullPath) {
    int dot = fullPath.lastIndexOf(".");
    int sep = fullPath.lastIndexOf(File.separator);
    return fullPath.substring(sep + 1, dot);
  }

  public static String getPath(String fullPath) {
    int sep = fullPath.lastIndexOf(File.separator);
    return fullPath.substring(0, sep);
  }
  
  /**
   * Return the file name of a file with the extension when provided
   * just a file name without the extension.
   * @param locationToSearch
   * @param filenameWithoutExt
   * @return filename with the extension
   */
  public static String findFilenameWOExt(String locationToSearch,
                                         String filenameWithoutExt) {
    //String locaitonToSearch = locationToSearch; // Give your folderName
    File[] listFiles = new File(locationToSearch).listFiles();

    for (int i = 0; i < listFiles.length; i++) {

        if (listFiles[i].isFile()) {
            String filename = getFilename(listFiles[i].getName());
            //String filename = listFiles[i].getName();
            String fileExt = getExtension(listFiles[i].getName());
            // Providing that the name has a match, and the overall length is as
            // expected, the file is a match. Return that one.
            if (filename.startsWith(filenameWithoutExt) &&
                filename.length() + fileExt.length() ==
                filenameWithoutExt.length() + fileExt.length()) {
              System.out.println("found file" + " " + filename);
              return listFiles[i].getName();
            }
        }
    }
    return "";
  }
}
