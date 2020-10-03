package shareMoor.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        // System.out.println(s);
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
   */
  public static void setUpExifToolCall(String programPath, String filePath, String file,
      String status) {
    String OS = getOS().toString();
    String cmd = "";
    String program = "";

    if (OS == "Linux" || OS == "MacOs" || OS == "Other") {
      program = "Image-ExifTool-12.06/exiftool.pl";
    }
    if (OS == "Windows") {
      program = "exiftool.exe";
    }
    cmd = programPath + program + " -b -createdate " + filePath + file;
    System.out.println(cmd);
    String createDate = callSystemProgram(cmd);
    cmd = "perl " + programPath + program + " -q -all= " + filePath + file;
    callSystemProgram(cmd);

    if (createDate.equals("")) {
      System.out.println("No create date found. Grabbing system time.");
      Date date = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
      createDate = formatter.format(date).toString();
    }
    createDate = createDate.replace(" ", "\b");

    cmd = programPath + program + " -createdate=" + createDate + " " + filePath + file;
    callSystemProgram(cmd);

    cmd = programPath + program + " -q -Keywords=" + status + " " + filePath + file;
    callSystemProgram(cmd);

    cmd = programPath + program + " -q -delete_original! " + filePath + file;
    callSystemProgram(cmd);

  }
  /*
   * // TODO: remove? // @depricated
   * 
   * public static long killProgram(String cmd, long PID) { String path = ""; OSType os = getOS();
   * switch (os) { case Windows: if (cmd.equals("kill ")) { cmd = "taskkill /F /PID "; } path = cmd
   * + PID; break; case MacOS: path = cmd + PID; break; case Linux: path = cmd + PID; break; case
   * Other: path = null; break; } try { if (path == null) { System.out.println("Platform " + os +
   * " not supported."); } else { Runtime run = Runtime.getRuntime(); Process proc = run.exec(path);
   * System.out.println("Calling path: \"" + path + PID + "\""); System.out.println(proc);
   * 
   * return proc.pid(); } } catch (IOException e) { e.printStackTrace(); } return -1;
   * 
   * }
   * 
   * public static long callExternalProgram(String cmd, String path) {
   * 
   * // Unixes assumes program is within $PATH. // TODO: either povide environment variable, or
   * provide path to executable. // TODO: check $PATH, or be sure to specify absolute path link
   * within windows. // TODO: address windows path needing to be abs or it can be realative. OSType
   * os = getOS(); switch (os) { case Windows: path = cmd + path; break; case MacOS: path = cmd +
   * path; break; case Linux: path = cmd + " " + path; break; case Other: path = null; break; } try
   * { if (path == null) { System.out.println("Platform " + os + " not supported."); } else {
   * Runtime run = Runtime.getRuntime(); Process proc = run.exec(path);
   * System.out.println("Calling path: \"" + path + "\""); System.out.println(proc);
   * 
   * return proc.pid(); } } catch (IOException e) { e.printStackTrace(); } return -1; }
   */
}
