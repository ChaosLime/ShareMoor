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
package tools;

import java.io.IOException;
import java.util.Locale;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
   * @author nick
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

  public static void getTheme(OSType ostype) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, UnsupportedLookAndFeelException {
    switch (ostype) {
      case Windows:
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        break;
      case MacOS:
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        break;
      case Linux:
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        break;
      case Other:
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        break;
    }
  }

  public static void setTheme() {
    OSType os = getOS();
    try {
      getTheme(os);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
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
        path = cmd + path;
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

  public static void init() {
    setTheme();
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
