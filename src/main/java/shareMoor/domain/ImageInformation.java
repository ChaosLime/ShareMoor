package shareMoor.domain;

/**
 * Share Moor
 * 
 * ImageInformation.java
 * 
 * Purpose: To hold basic information about an image uploaded for the purposes of correctly creating
 * a thumbnail in the correct orientation. Used code from
 * https://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
 * 
 * @author Mitchell Saunders
 *
 */
public class ImageInformation {
  public final int orientation;
  public final int width;
  public final int height;

  /**
   * Constructor that initializes all pertinent information regarding the image.
   * 
   * @param orientation int holds one of the 8 configurations that the image is saved in.
   * @param width int holds the number of pixels this image is wide.
   * @param height int holds the number of pixels this image is tall.
   */
  public ImageInformation(int orientation, int width, int height) {
    this.orientation = orientation;
    this.width = width;
    this.height = height;
  }

  /**
   * Defines object toString() method.
   * 
   * @return Readable output that represents the contents of this class.
   */
  public String toString() {
    return String.format("%dx%d,%d", this.width, this.height, this.orientation);
  }
}
