// https://spring.io/guides/gs/uploading-files/
// https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import shareMoor.domain.ConfigHandler;
import shareMoor.domain.HelperClass;
import shareMoor.exception.StorageException;
import shareMoor.exception.StorageFileNotFoundException;

@Service
public class FileSystemStorageService implements StorageService {

  // TODO: Create a counter and a method in this service that will be responsible
  // for finding the next file number to use.

  private final Path uploadLocation;
  private final Path finishedFullLocation;
  private final Path finishedThumbLocation;
  private final Path deniedLocation;
  private final Path reviewThumbLocation;
  private final Path assestsLocation;

  private int fileCounter = 1;

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.uploadLocation = Paths.get(properties.getUploadLocation());
    this.finishedFullLocation = Paths.get(properties.getFinishedFullLocation());
    this.finishedThumbLocation = Paths.get(properties.getFinishedThumbLocation());
    this.deniedLocation = Paths.get(properties.getDeniedLocation());
    this.reviewThumbLocation = Paths.get(properties.getReviewThumbLocation());
    this.assestsLocation = Paths.get(StorageProperties.getAssestsLocation());
  }

  @Override
  public String store(MultipartFile file) {
    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
    String newFilename = "";
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + originalFilename);
      }
      if (originalFilename.contains("..")) {
        // This is a security check
        throw new StorageException(
            "Cannot store file with relative path outside current directory " + originalFilename);
      }

      /*
       * If the extension is either not defined within the config file, or its status is true, it
       * will not be uploaded. Its type will also be checked to see if they accept the file type.
       */
      String ext = HelperClass.getExtension(originalFilename).toString();
      String extType = ConfigHandler.checkExtType(ext);
      boolean isValidType = ConfigHandler.checkTypeStatus(extType);
      boolean isExt = ConfigHandler.checkExtStatus(ext);

      if (isExt && isValidType) {
        try (InputStream inputStream = file.getInputStream()) {
          newFilename = String.valueOf(fileCounter) + HelperClass.getExtension(originalFilename);
          fileCounter++;

          Files.copy(inputStream, this.uploadLocation.resolve(newFilename),
              StandardCopyOption.REPLACE_EXISTING);
        }
      } else {
        return null;
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + newFilename, e);
    }

    return this.uploadLocation + File.separator + newFilename;
  }

  @Override
  public Stream<Path> loadAllFinishedThumbs() {
    try {
      return Files.walk(this.finishedThumbLocation, 1)
          .filter(path -> !path.equals(this.finishedThumbLocation))
          .map(this.finishedThumbLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Stream<Path> loadAllReviewThumbs() {
    try {
      return Files.walk(this.reviewThumbLocation, 1)
          .filter(path -> !path.equals(this.reviewThumbLocation))
          .map(this.reviewThumbLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Stream<Path> loadAllFinishedFull() {
    try {
      return Files.walk(this.finishedFullLocation, 1)
          .filter(path -> !path.equals(this.finishedFullLocation))
          .map(this.finishedFullLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Stream<Path> loadAllReviewFull() {
    try {
      return Files.walk(this.uploadLocation, 1).filter(path -> !path.equals(this.uploadLocation))
          .map(this.uploadLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Path loadFinishedThumb(String filename) {
    return finishedThumbLocation.resolve(filename);
  }

  @Override
  public Path loadReviewThumb(String filename) {
    return reviewThumbLocation.resolve(filename);
  }

  @Override
  public Path loadFinishedFull(String filename) {
    // Code will grab file name in the finsihed full location that corrleates with the
    // filename in the thumbnail folder.
    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt =
        HelperClass.findFilenameWOExt(finishedFullLocation.toString(), filenameWithoutExt);

    return finishedFullLocation.resolve(filenameWithExt);
  }

  @Override
  public Path loadReviewFull(String filename) {
    // Code will grab file name in the finsihed full location that corrleates with the
    // filename in the thumbnail folder.
    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt =
        HelperClass.findFilenameWOExt(uploadLocation.toString(), filenameWithoutExt);

    return uploadLocation.resolve(filenameWithExt);
  }

  @Override
  public Resource loadAsResourceFinishedFull(String filename) {
    try {
      Path file = loadFinishedFull(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public Resource loadAsResourceFinishedThumbs(String filename) {
    try {
      Path file = loadFinishedThumb(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public Resource loadAsResourceReviewFull(String filename) {
    try {
      Path file = loadReviewFull(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public Resource loadAsResourceReviewThumbs(String filename) {
    try {
      Path file = loadReviewThumb(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(uploadLocation.toFile());
    FileSystemUtils.deleteRecursively(finishedFullLocation.toFile());
    FileSystemUtils.deleteRecursively(deniedLocation.toFile());
    FileSystemUtils.deleteRecursively(finishedThumbLocation.toFile());
    FileSystemUtils.deleteRecursively(reviewThumbLocation.toFile());
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(uploadLocation);
      Files.createDirectories(finishedFullLocation);
      Files.createDirectories(deniedLocation);
      Files.createDirectories(finishedThumbLocation);
      Files.createDirectories(reviewThumbLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  public Resource loadAsResourceAssest(String filename) {
    try {
      Path file = loadAssest(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public Path loadAssest(String filename) {

    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt =
        HelperClass.findFilenameWOExt(assestsLocation.toString(), filenameWithoutExt);

    return assestsLocation.resolve(filenameWithExt);
  }

  /**
   * Grab correct content type from the filename and extension.
   * 
   * @param filename String contains just the filename and extension of the file that is attempting
   *        to be downloaded.
   * @return
   */
  public String getMimeType(String filename) {
    Path path = new File(filename).toPath();
    String mimeType = "application/octet-stream";
    try {
      mimeType = Files.probeContentType(path);
    } catch (IOException e) {
      System.out.println("Unable to get mimeType from file. Set as default");
    }
    return mimeType;
  }

  // Don't need helper functions apparently. To find mimetype, full path to file
  // isn't actually required. Java can determine mimetype from file path containing
  // name and file extension only.
  /*
   * public String getMimeTypeFinishedFull(String filename) { return getMimeType(filename,
   * finishedFullLocation.toString()); }
   * 
   * public String getMimeTypeReviewFull(String filename) { return getMimeType(filename,
   * uploadLocation.toString()); }
   */
}
