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

  Stream<Path> loadAllFull();
  
  Stream<Path> loadAllThumbs();

  Path loadFull(String filename);
  
  Path loadThumb(String filename);

  Resource loadAsResourceFull(String filename);

  void deleteAll();

  Resource loadAsResourceThumbs(String filename);
}
