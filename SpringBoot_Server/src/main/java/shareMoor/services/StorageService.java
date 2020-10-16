/*
 * This file is part of Share Moor
 * 
 * Share Moor is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Share Moor is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Share Moor. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
