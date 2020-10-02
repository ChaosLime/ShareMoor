// https://stackoverflow.com/questions/1069095/how-do-you-create-a-thumbnail-image-out-of-a-jpeg-in-java
package shareMoor.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import shareMoor.domain.HelperClass;
import shareMoor.domain.ImageInformation;
import shareMoor.exception.StorageException;

@Service
public class ThumbnailService {

  private final Path reviewThumbLocation;
  private final Path assestsLocation;

  public final String imageExtTxt = ".png";

  @Autowired
  public ThumbnailService(StorageProperties properties) {
    this.reviewThumbLocation = Paths.get(properties.getReviewThumbLocation());
    this.assestsLocation = Paths.get(properties.getAssestsLocation());

  }

  public void createThumbnail(String filePath) {// throws IOException {
    // TODO: Check to see if this filetype is an image. If so then proceed.
    // This will be done as a check of the application.yaml file that
    // will contain valid file types.
    // If not, then set as a generic thumbnail which I need to make and put in the assets.

    BufferedImage img;

    final ImageFormat desiredImageFormat = ImageFormats.PNG;

    File file = new File(filePath);

    ImageInformation imageInfo = null;

    try {
      imageInfo = HelperClass.readImageInformation(file);
      System.out.println(imageInfo.toString());
    } catch (MetadataException | ImageProcessingException | IOException | NullPointerException e1) {
      System.out.println("Failed to read metadata on " + filePath);
      // e1.printStackTrace();
    }

    try {
      // img = Imaging.getBufferedImage(file);
      img = ImageIO.read(file);

      if (imageInfo != null) {
        img = HelperClass.correctOrientation(img, imageInfo);
      }
      if (img == null) {
        throw new IOException();
      }
    } catch (IOException e) { // | ImageReadException e) {
      // If it fails, then use default thumbnail
      System.out.println("Failed to make thumbnail of file. Error message: " + e.getMessage());
      System.out.println("Using default thumbnail...");
      // Template thumbnail image for all things that cannot be resolved to BufferedImage obj.
      img = getDefaultThumbnail();
    }

    try {
      img = Scalr.resize(img, Method.AUTOMATIC, 250);
    } catch (Exception e) {
      throw new StorageException("Unable to scale image.", e);
    }

    String filenameWithoutExt = HelperClass.getFilename(filePath.toString());

    try {
      Imaging.writeImage(img,
          new File(reviewThumbLocation + File.separator + filenameWithoutExt + imageExtTxt),
          desiredImageFormat, null);
    } catch (IOException | ImageWriteException e) {
      throw new StorageException("Unable to save thumbnail.", e);
    }
  }

  private BufferedImage getDefaultThumbnail() {
    BufferedImage img;
    try {
      File file = new File(assestsLocation + File.separator + "fileIcon.jpg");
      img = Imaging.getBufferedImage(file);
    } catch (IOException | ImageReadException e) {
      throw new StorageException("Failed to retrieve fileIcon.jpg from resoruces folder.", e);
    }
    return img;
  }
}
