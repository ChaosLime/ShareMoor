package shareMoor.domain;

import java.io.IOException;
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

  public static long callExternalProgram(String cmd, String path) {

    // Unixes assumes program is within $PATH.
    // TODO: check $PATH, or be sure to specify absolute path link within windows.
    // TODO: address windows path needing to be abs or it can be realative.
    OSType os = getOS();
    switch (os) {
      case Windows:
        path = cmd + path;
        break;
      case MacOS:
        path = cmd + path;
        break;
      case Linux:
        path = cmd + " " + path;
        break;
      case Other:
        path = null;
        break;
    }
    try {
      if (path == null) {
        System.out.println("Platform " + os + " not supported.");
      } else {
        Runtime run = Runtime.getRuntime();
        Process proc = run.exec(path);
        System.out.println("Calling path: \"" + path + "\"");
        System.out.println(proc);

        return proc.pid();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public static String callWinProgram(String cmd) {

    Runtime run = Runtime.getRuntime();
    
    Process proc = null;
    try {
      proc = run.exec(cmd);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Calling path: \"" + cmd + "\"");
    
    //TODO: Figured out how to read windows output streams for the creation date
    //rather than writing a new one upon creation.
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String datetime = formatter.format(date);
    return datetime;
  }



  public static long killProgram(String cmd, long PID) {
    String path = "";
    OSType os = getOS();
    switch (os) {
      case Windows:
        if (cmd.equals("kill ")) {
          cmd = "taskkill /F /PID ";
        }
        path = cmd + PID;
        break;
      case MacOS:
        path = cmd + PID;
        break;
      case Linux:
        path = cmd + PID;
        break;
      case Other:
        path = null;
        break;
    }
    try {
      if (path == null) {
        System.out.println("Platform " + os + " not supported.");
      } else {
        Runtime run = Runtime.getRuntime();
        Process proc = run.exec(path);
        System.out.println("Calling path: \"" + path + PID + "\"");
        System.out.println(proc);

        return proc.pid();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;

  }
}
