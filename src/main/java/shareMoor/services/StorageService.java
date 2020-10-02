// https://spring.io/guides/gs/uploading-files/
// https://github.com/spring-guides/gs-uploading-files
package shareMoor.services;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  void init();

  String store(MultipartFile file);

  Stream<Path> loadAllFull();

  Stream<Path> loadAllThumbs();

  Path loadFull(String filename);

  Path loadThumb(String filename);

  Path loadAssest(String filename);

  Resource loadAsResourceAssest(String filename);

  Resource loadAsResourceFull(String filename);

  void deleteAll();

  Resource loadAsResourceThumbs(String filename);

  Stream<Path> loadAllFinishedFull();

  Stream<Path> loadAllFinishedThumbs();

  Path loadFinishedFull(String filename);

  Path loadFinishedThumb(String filename);

  Resource loadAsResourceFinishedFull(String filename);

  Resource loadAsResourceFinishedThumbs(String filename);

  Stream<Path> loadAllReviewFull();

  Stream<Path> loadAllReviewThumbs();

  Path loadReviewThumb(String filename);

  Path loadReviewFull(String filename);

  Resource loadAsResourceReviewFull(String filename);

  Resource loadAsResourceReviewThumbs(String filename);

}
