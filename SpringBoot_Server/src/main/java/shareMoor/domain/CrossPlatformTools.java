package shareMoor.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;


public class CrossPlatformTools {

  public enum OSType {
    Windows, MacOS, Linux, Other
  };

  // cached result of OS detection
  protected static OSType detectedOS;

  /**
   * detect the operating system from the os.name System property and cache the result
   * 
   * @returns - the operating system detected
   */
  public static OSType getOS() {
    if (detectedOS == null) {
      String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
      if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
        detectedOS = OSType.MacOS;
      } else if (OS.indexOf("win") >= 0) {
        detectedOS = OSType.Windows;
      } else if (OS.indexOf("nux") >= 0) {
        detectedOS = OSType.Linux;
      } else {
        detectedOS = OSType.Other;
      }
    }
    return detectedOS;
  }


  /**
   * Calls a system program given by a String command prompt. Returns the output of the string
   * buffer if valid or required.
   * 
   * @param cmd
   * @return
   */
  public static String callSystemProgram(String cmd) {
    Runtime run = Runtime.getRuntime();
    String result = "";
    try {
      Process proc = run.exec(cmd);
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      String s = null;

      while ((s = stdInput.readLine()) != null) {
        result += s;
      }

      stdInput.close();
      return result;

    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * This function allows for a system call to be provided given their paths relative or absolute in
   * a cross platform manner. Currently only handles exiftool useage.
   * 
   * @param programPath
   * @param filePath
   * @param file
   * @param status
   * @return
   */
  public static String setUpExifToolCall(String dir) {
    String OS = getOS().toString().toLowerCase();
    String program = "";

    if (OS.equals("linux") || OS.equals("macos") || OS.equals("other")) {
      program = "perl " + dir + "/Image-ExifTool-12.06/exiftool.pl";
    }
    if (OS.equals("windows")) {
      program = dir + "exiftool.exe";
    }

    return program;
  }

}
