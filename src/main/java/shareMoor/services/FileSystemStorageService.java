//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files
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
import shareMoor.domain.HelperClass;
import shareMoor.exception.StorageException;
import shareMoor.exception.StorageFileNotFoundException;

@Service
public class FileSystemStorageService implements StorageService {

  // TODO: Create a counter and a method in this service that will be responsible
  //        for finding the next file number to use.
  
  private final Path uploadLocation;
  private final Path finishedFullLocation;
  private final Path finishedThumbLocation;
  private final Path needsReviewLocation;
  private final Path reviewThumbLocation;
  
  private int fileCounter = 1;

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.uploadLocation = Paths.get(properties.getUploadLocation());
    this.finishedFullLocation = Paths.get(properties.getFinishedFullLocation());
    this.finishedThumbLocation = Paths.get(properties.getFinishedThumbLocation());
    this.needsReviewLocation = Paths.get(properties.getDeniedLocation());
    this.reviewThumbLocation = Paths.get(properties.getReviewThumbLocation());
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
            "Cannot store file with relative path outside current directory "
                + originalFilename);
      }
      
      // TODO: Check that the file type is acceptable, if not, then just skip file.
      // optionally, provide useful error message back to the webpage
      try (InputStream inputStream = file.getInputStream()) {
        newFilename = String.valueOf(fileCounter) + HelperClass.getExtension(originalFilename);
        fileCounter++;
        
        Files.copy(inputStream, this.uploadLocation.resolve(newFilename),
                  StandardCopyOption.REPLACE_EXISTING);
      }
    }
    catch (IOException e) {
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
    }
    catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }
  
  @Override
  public Stream<Path> loadAllReviewThumbs() {
    try {
      return Files.walk(this.reviewThumbLocation, 1)
        .filter(path -> !path.equals(this.reviewThumbLocation))
        .map(this.reviewThumbLocation::relativize);
    }
    catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }
  
  @Override
  public Stream<Path> loadAllFinishedFull() {
    try {
      return Files.walk(this.finishedFullLocation, 1)
        .filter(path -> !path.equals(this.finishedFullLocation))
        .map(this.finishedFullLocation::relativize);
    }
    catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }
  
  @Override
  public Stream<Path> loadAllReviewFull() {
    try {
      return Files.walk(this.uploadLocation, 1)
        .filter(path -> !path.equals(this.uploadLocation))
        .map(this.uploadLocation::relativize);
    }
    catch (IOException e) {
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
    String filenameWithExt = HelperClass.findFilenameWOExt(finishedFullLocation.toString(),
                                                          filenameWithoutExt);
    
    return finishedFullLocation.resolve(filenameWithExt);
  }
  
  @Override
  public Path loadReviewFull(String filename) {
    // Code will grab file name in the finsihed full location that corrleates with the
    // filename in the thumbnail folder.
    String filenameWithoutExt = HelperClass.getFilename(filename);
    String filenameWithExt = HelperClass.findFilenameWOExt(uploadLocation.toString(),
                                                          filenameWithoutExt);
    
    return uploadLocation.resolve(filenameWithExt);
  }

  @Override
  public Resource loadAsResourceFinishedFull(String filename) {
    try {
      Path file = loadFinishedFull(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      }
      else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);

      }
    }
    catch (MalformedURLException e) {
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
      }
      else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);

      }
    }
    catch (MalformedURLException e) {
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
      }
      else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);

      }
    }
    catch (MalformedURLException e) {
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
      }
      else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);

      }
    }
    catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(uploadLocation.toFile());
    FileSystemUtils.deleteRecursively(finishedFullLocation.toFile());
    FileSystemUtils.deleteRecursively(needsReviewLocation.toFile());
    FileSystemUtils.deleteRecursively(finishedThumbLocation.toFile());
    FileSystemUtils.deleteRecursively(reviewThumbLocation.toFile());
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(uploadLocation);
      Files.createDirectories(finishedFullLocation);
      Files.createDirectories(needsReviewLocation);
      Files.createDirectories(finishedThumbLocation);
      Files.createDirectories(reviewThumbLocation);
    }
    catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }
}
