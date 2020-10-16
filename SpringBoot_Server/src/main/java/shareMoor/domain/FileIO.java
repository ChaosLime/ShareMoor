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
package shareMoor.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileIO {
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

  public static void writeStrBufferToNewFile(String path, String sb) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(path));
      writer.write(sb);
      writer.flush();
      writer.close();
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

  /**
   * Handles unzipping of a directory specfied.
   * 
   * @param source
   * @param target
   * @throws IOException
   */
  public static void unzip(Path source, Path target) throws IOException {

    try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(source.toFile()))) {

      ZipEntry zipEntry = zipInputStream.getNextEntry();

      while (zipEntry != null) {

        boolean isDirectory = false;

        if (zipEntry.getName().endsWith(File.separator)) {
          isDirectory = true;
        }

        Path newPath = zipSlipProtect(zipEntry, target);

        if (isDirectory) {
          Files.createDirectories(newPath);
        } else {

          if (newPath.getParent() != null) {
            if (Files.notExists(newPath.getParent())) {
              Files.createDirectories(newPath.getParent());
            }
          }
          Files.copy(zipInputStream, newPath, StandardCopyOption.REPLACE_EXISTING);
        }

        zipEntry = zipInputStream.getNextEntry();

      }
      zipInputStream.closeEntry();

    }

  }

  /**
   * This is a protection found to handle zip slip attacks.
   * https://snyk.io/research/zip-slip-vulnerability
   * 
   * @param zipEntry
   * @param targetDir
   * @return
   * @throws IOException
   */
  public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {

    Path targetDirResolved = targetDir.resolve(zipEntry.getName());

    Path normalizePath = targetDirResolved.normalize();
    if (!normalizePath.startsWith(targetDir)) {
      throw new IOException("Bad zip entry: " + zipEntry.getName());
    }

    return normalizePath;
  }
}
