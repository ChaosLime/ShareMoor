package shareMoor.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;


/**
 * Share Moor
 * 
 * HelperClass.java
 * 
 * Purpose: To provide functional services to various services and classes throughout the server
 * that did not necessarily belong anywhere else.
 * 
 * @author Mitchell Saunders
 *
 */
public class HelperClass {

  /**
   * String manipulator that will exract the file extension when provided a full file path or
   * filename.
   * 
   * @param fullPath String is either the filename or file path.
   * @return file extension starting at the '.' character to the end of the filename.
   */
  public static String getExtension(String fullPath) {
    int dot = fullPath.lastIndexOf(".");
    return fullPath.substring(dot);
  }

  /**
   * String manipulator that extracts the filename without the extension and without the path that
   * precedes it. This has also been adapted to handle all types of file separators.
   * 
   * @param fullPath String can either be the file path, filename with or without the extension.
   * @return the bare filename without any file path or file extension.
   */
  public static String getFilename(String fullPath) {
    int dot = fullPath.lastIndexOf(".");
    if (dot == -1) {
      dot = fullPath.length();
    }

    int sep1 = fullPath.lastIndexOf(File.separator);
    int sep2 = fullPath.lastIndexOf("/");
    int sep3 = fullPath.lastIndexOf("\\");

    int max = Integer.max(sep1, sep2);
    max = Integer.max(max, sep3);

    return fullPath.substring(max + 1, dot);
  }

  /**
   * String manipulator that extracts the file path from the full filename and path.
   * 
   * @param fullPath String which needs to at least contain a single file separator that may contain
   *        a filename.
   * @return the file path without the file name.
   */
  public static String getPath(String fullPath) {
    int sep = fullPath.lastIndexOf(File.separator);
    return fullPath.substring(0, sep);
  }

  /**
   * <<<<<<< HEAD Given the filename without an extension, this method will search a defined
   * location for a filename that matches it in that new directory. This method was designed to
   * allow this program identify a full file by it's thumbnail photo, since they by convention have
   * the same filename, but may have different file extensions and file paths.
   * 
   * @param locationToSearch String is a directory that should contain a file to be searched for.
   * @param filenameWithoutExt String is a filename without the extension that will likely be found
   *        in the location defined by the first parameter.
   * @return filename with the extension of the file in the folder defined by the first parameter.
   *         ======= Return the file name of a file with the extension when provided just a file
   *         name without the extension.
   * 
   * @param locationToSearch
   * @param filenameWithoutExt
   * @return filename with the extension >>>>>>> exiftool
   */
  public static String findFilenameWOExt(String locationToSearch, String filenameWithoutExt) {
    // String locaitonToSearch = locationToSearch; // Give your folderName
    File[] listFiles = new File(locationToSearch).listFiles();

    for (int i = 0; i < listFiles.length; i++) {

      if (listFiles[i].isFile()) {
        String filename = getFilename(listFiles[i].getName());

        String fileExt = getExtension(listFiles[i].getName());
        // Providing that the name has a match, and the overall length is as
        // expected, the file is a match. Return that one.
        if (filename.startsWith(filenameWithoutExt) && filename.length()
            + fileExt.length() == filenameWithoutExt.length() + fileExt.length()) {

          System.out.println("found file" + " " + filename);

          return listFiles[i].getName();
        }
      }
    }
    return "";
  }

  /**
   * String manipulation that is designed to clean up some extra characters added to Optional
   * parameters. This method will clean the "Optional[" from the beginning of the filePath, and the
   * "]" from the end if it exists.
   * 
   * @param filePath String is the file path that may contain the extra characters.
   * @return cleanedFilePath String is the cleaned file path or name that had any of the extra
   *         characters.
   */
  public static String cleanOptionalFilePath(String filePath) {
    String firstHalf = "Optional[";
    String secondHalf = "]";
    if (filePath.contains(firstHalf)) {
      filePath = filePath.substring(firstHalf.length(), filePath.length());
    }
    if (filePath.contains(secondHalf)) {
      filePath = filePath.substring(0, filePath.length() - secondHalf.length());
    }
    return filePath;
  }

  /**
   * Reads metadata information from image files. This is required to determine the orientation of a
   * file, which is needed for properly rotating a thumbnail.
   * 
   * @param imageFile File is the file on the webserver that needs the metadata read.
   * @return new ImageInformation that contains resolution and image orientation only.
   * @throws IOException
   * @throws MetadataException
   * @throws ImageProcessingException
   */

  public static ImageInformation readImageInformation(File imageFile)
      throws IOException, MetadataException, ImageProcessingException {
    Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
    Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
    JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

    int orientation = 1;
    try {
      orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
    } catch (MetadataException me) {
      System.out.println("Could not get orientation");
    }
    int width = jpegDirectory.getImageWidth();
    int height = jpegDirectory.getImageHeight();

    return new ImageInformation(orientation, width, height);
  }

  /**
   * Corrects an image's orientation. Used code from
   * https://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
   * as basis for this adaptaion. More information can be found at http://chunter.tistory.com/143
   * and https://jdhao.github.io/2019/07/31/image_rotation_exif_info/
   * 
   * @param bImg BufferedImage which contains the data for a thumbnail that may or may not need
   *        orientation correction.
   * @param info ImageInformation which holds basic information about the photo, such as it's
   *        orientation.
   * @return bImg BufferedImage that may have had it's orientation corrected.
   */
  public static BufferedImage correctOrientation(BufferedImage bImg, ImageInformation info) {
    switch (info.orientation) {
      case 1:
        System.out.println("Image correctly oriented. No action required. Case 1.");
        break;
      case 2: // Flip X
        // bImg = Scalr.rotate(bImg, Rotation.FLIP_VERT);
        // t.scale(-1.0, 1.0);
        // t.translate(-info.width, 0);
        System.out.println("Image mirrored over y-axis. Not implemented. Case 2.");
        break;
      case 3: // PI rotation
        bImg = Scalr.rotate(bImg, Rotation.CW_180);
        System.out.println("Rotated 180. Corrected with 180 deg rotation. Case 3.");
        break;
      case 4: // Flip Y
        System.out.println("Image rotated 180, and mirrored over x-axis. Not implemented. Case 4.");
        break;
      case 5: // - PI/2 and Flip X
        // t.rotate(-Math.PI / 2);
        // t.scale(-1.0, 1.0);
        System.out.println("Rotated 180. Corrected with 180 deg rotation. Case 3.");
        break;
      case 6: // -PI/2 and -width
        bImg = Scalr.rotate(bImg, Rotation.CW_90);
        System.out.println("Image rotated 270. Corrected with 90 deg rotation. Case 6.");
        break;
      case 7: // PI/2 and Flip
        // t.scale(-1.0, 1.0);
        // t.translate(-info.height, 0);
        // t.translate(0, info.width);
        // t.rotate(3 * Math.PI / 2);
        System.out.println("Image rotated 90, and mirrored over x-axis. Not implemented. Case 7.");
        break;
      case 8: // PI / 2
        // t.translate(0, info.width);
        // t.rotate(3 * Math.PI / 2);
        bImg = Scalr.rotate(bImg, Rotation.CW_270);
        System.out.println("Image rotated 90. Corrected with 270 deg rotation. Case 8.");
        break;
    }
    return bImg;
  }

  /*
   * public static boolean IsThisFileAnImage(File f) { String mimetype = new
   * MimetypesFileTypeMap().getContentType(f); String type = mimetype.split("/")[0]; if
   * (type.equals("image")) System.out.println("It's an image"); else
   * System.out.println("It's NOT an image"); }
   */

}
