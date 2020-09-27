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
  
  // Look at http://chunter.tistory.com/143 for information or https://jdhao.github.io/2019/07/31/image_rotation_exif_info/
  // Code came from https://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
  // but was modified for use in the context of this application.
  public static BufferedImage CorrectOrientation(BufferedImage bImg, ImageInformation info) {

    switch (info.orientation) {
      case 1:
        System.out.println("Image correctly oriented. No action required. Case 1.");
        break;
      case 2: // Flip X
        //bImg = Scalr.rotate(bImg, Rotation.FLIP_VERT); 
        //t.scale(-1.0, 1.0);
        //t.translate(-info.width, 0);
        System.out.println("Image mirrored over y-axis. Not implemented. Case 2.");
        break;
      case 3: // PI rotation
        bImg = Scalr.rotate(bImg, Rotation.CW_180);
        //t.translate(info.width, info.height);
        //t.rotate(Math.PI);
        //System.out.println("Request a PI rotation. Not implemented. Case 3.");
        System.out.println("Rotated 180. Corrected with 180 deg rotation. Case 3.");
        break;
      case 4: // Flip Y
        //bImg = Scalr.rotate(bImg, Rotation.FLIP_HORZ);
        //t.scale(1.0, -1.0);
        //t.translate(0, -info.height);
        System.out.println("Image rotated 180, and mirrored over x-axis. Not implemented. Case 4.");
        break;
      case 5: // - PI/2 and Flip X
        //t.rotate(-Math.PI / 2);
        //t.scale(-1.0, 1.0);
        System.out.println("Image rotated 270, and mirrored over x-axis. Not implemented. Case 5.");
        break;
      case 6: // -PI/2 and -width
        bImg = Scalr.rotate(bImg, Rotation.CW_90);
        //t.translate(info.height, 0);
        //t.rotate(Math.PI / 2);
        System.out.println("Image rotated 270. Corrected with 90 deg rotation. Case 6.");
        break;
      case 7: // PI/2 and Flip
        //t.scale(-1.0, 1.0);
        //t.translate(-info.height, 0);
        //t.translate(0, info.width);
        //t.rotate(3 * Math.PI / 2);
        System.out.println("Image rotated 90, and mirrored over x-axis. Not implemented. Case 7.");
        break;
      case 8: // PI / 2
        bImg = Scalr.rotate(bImg, Rotation.CW_270);
        //t.translate(0, info.width);
        //t.rotate(3 * Math.PI / 2);
        System.out.println("Image rotated 90. Corrected with 270 deg rotation. Case 8.");
        break;
    }
    return bImg;
  }
}
