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

  Path loadAssest(String filename);

  Resource loadAsResourceAssest(String filename);

  void deleteAll();

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

  String getMimeType(String filename);

  String checkIfEmpty(MultipartFile file);

  int countFilesInFinishedFullDir();

}