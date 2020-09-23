// https://stackoverflow.com/questions/1069095/how-do-you-create-a-thumbnail-image-out-of-a-jpeg-in-java
package shareMoor.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

// TODO: Need to consider re-writing to become a batched process for creating thumbnails
// see https://nirajsonawane.github.io/2018/11/08/Spring-Batch-Process-Multiple-Files-Parallel/


@Service
public class ThumbnailService {
  public void saveThumbnail(String filePath) throws IOException {
    // TODO: Check to see if this filetype is an image. If so then proceed.
    // If not, then set as a generic thumbnail which I need to make and put in the assets.
    BufferedImage img = ImageIO.read(new File(filePath));
    
    img = Scalr.resize(img, 250);
    
    String filenameWithoutExt = filePath.replaceFirst("[.][^.]+$",  "");
    
    ImageIO.write(img, "jpg", new File(filenameWithoutExt + "_thumb.jpg"));
  }
}
