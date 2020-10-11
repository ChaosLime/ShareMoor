package tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class GenerateQRCode {
  // TODO: figure out appropriate size for QR codes
  // perhaps make it large and scale back?
  static int sizeOfQRCodes = 1000;

  public static void webSiteAddress(String qrCodeText) {
    // TODO: correct path to setable within config.
    String fileName = "websiteQR.png";
    String dir = ConfigHelper.getSettingsDir("assests-dir");
    String filePath = dir + File.separator + fileName;
    String fileType = "png";

    File file = new File(dir);

    if (file.mkdirs()) {
      System.out.println("Directory is created!");
    }

    File qrFile = new File(filePath);
    try {
      createQRImage(qrFile, qrCodeText, sizeOfQRCodes, fileType);
    } catch (WriterException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Generating website QR code.");
  }

  public static void wifiAccess() {
    String SSID = "";
    SSID = ConfigHelper.getSSID();
    String wifiPassword = "";
    wifiPassword = ConfigHelper.getWifiPass();
    String encryptionType = "";
    encryptionType = ConfigHelper.getEncryptionType();
    String text = "WIFI:T:" + encryptionType + ";S:" + SSID + ";P:" + wifiPassword;

    // TODO: correct path to setable within config.
    String fileName = "wifiQR.png";
    String dir = ConfigHelper.getSettingsDir("assests-dir");
    String filePath = dir + File.separator + fileName;
    String fileType = "png";
    File qrFile = new File(filePath);
    try {
      createQRImage(qrFile, text, sizeOfQRCodes, fileType);
    } catch (WriterException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Generating Wifi QR code.");
  }

  private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
      throws WriterException, IOException {
    // Create the ByteMatrix for the QR-Code that encodes the given String
    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix byteMatrix =
        qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
    // Make the BufferedImage that are to hold the QRCode
    int matrixWidth = byteMatrix.getWidth();
    BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
    image.createGraphics();

    Graphics2D graphics = (Graphics2D) image.getGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, matrixWidth, matrixWidth);
    // Paint and save the image using the ByteMatrix
    graphics.setColor(Color.BLACK);

    for (int i = 0; i < matrixWidth; i++) {
      for (int j = 0; j < matrixWidth; j++) {
        if (byteMatrix.get(i, j)) {
          graphics.fillRect(i, j, 1, 1);
        }
      }
    }
    ImageIO.write(image, fileType, qrFile);
  }

}
