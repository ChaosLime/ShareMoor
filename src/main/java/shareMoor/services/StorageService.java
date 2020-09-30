//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

  void init();

  String store(MultipartFile file);

  Stream<Path> loadAllFinishedFull();
  
  Stream<Path> loadAllFinishedThumbs();

  Path loadFinishedFull(String filename);
  
  Path loadFinishedThumb(String filename);

  Resource loadAsResourceFinishedFull(String filename);

  void deleteAll();

  Resource loadAsResourceFinishedThumbs(String filename);

  Stream<Path> loadAllReviewFull();

  Stream<Path> loadAllReviewThumbs();

  Path loadReviewThumb(String filename);

  Path loadReviewFull(String filename);

  Resource loadAsResourceReviewFull(String filename);

  Resource loadAsResourceReviewThumbs(String filename);
}
