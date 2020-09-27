// code came from https://stackoverflow.com/questions/5905868/how-to-rotate-jpeg-images-based-on-the-orientation-metadata
package shareMoor.domain;

//Inner class containing image information
public class ImageInformation {
 public final int orientation;
 public final int width;
 public final int height;

 public ImageInformation(int orientation, int width, int height) {
     this.orientation = orientation;
     this.width = width;
     this.height = height;
 }

 public String toString() {
     return String.format("%dx%d,%d", this.width, this.height, this.orientation);
 }
}