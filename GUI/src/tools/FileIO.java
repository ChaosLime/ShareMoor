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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
  /**
   * Reading of a file and putting it into a String Buffer.
   * 
   * @param path
   * @return
   */
  public static String getStrFromFile(String path) {
    BufferedReader br = null;
    FileReader fr = null;
    StringBuffer stringBuffer = new StringBuffer();
    try {
      fr = new FileReader(path);
      br = new BufferedReader(fr);
      String sCurrentLine;

      while ((sCurrentLine = br.readLine()) != null) {
        stringBuffer.append(sCurrentLine);
        stringBuffer.append("\n");

      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return stringBuffer.toString();
  }

  /**
   * By providing a path containing the file name along with a String buffer to be written, It will
   * be written to location.
   * 
   * @param path
   * @param sb
   */
  public static void writeStrBufferToNewFile(String path, String sb) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path));
      writer.write(sb);
      writer.flush();
      writer.close();
      // TODO remove?
      System.out.println("Saving file at: [" + path + "]");

    } catch (IOException e) {

      System.out.println("Content of StringBuffer failed.");
      e.printStackTrace();
    }

  }

  public static boolean checkIfFileExists(String path) {
    File f = new File(path);
    if (f.exists() && !f.isDirectory()) {
      return true;
    } else {
      return false;
    }
  }
}
