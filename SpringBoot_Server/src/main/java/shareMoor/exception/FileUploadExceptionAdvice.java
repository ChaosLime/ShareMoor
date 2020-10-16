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

// https://www.baeldung.com/spring-maxuploadsizeexceeded

package shareMoor.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class FileUploadExceptionAdvice {

  @Autowired
  private Environment env;

  // Moved here from the controller.
  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
  }

  // Deals with the exception generated by the Tomcat server when either files are too large
  // or the request is too large.
  // However, it fails to successfully return an error message to the user if the request size is
  // too large. This will need to be addressed at some point.
  // For now, it is probably best to set request size to -1 (unlimited), and implement a file
  // count limit.
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException exc,
      HttpServletRequest request, HttpServletResponse response) {

    String maxFileSize = getPropertyValue("spring.servlet.multipart.max-file-size");
    String maxRequestSize = getPropertyValue("spring.servlet.multipart.max-request-size");
    if (maxFileSize == "-1") {
      maxFileSize = "inf.";
    }
    if (maxRequestSize == "-1") {
      maxRequestSize = "inf.";
    }

    String errorMessage = "One of the files selected is too large!     "
        + "The file size maximum is " + maxFileSize + ".     "
        + "The total size of files that can be uploaded must not exceed " + maxRequestSize + ".";

    ModelAndView modelAndView = new ModelAndView("uploadForm/normal");
    modelAndView.getModel().put("errorMessage", errorMessage);
    return modelAndView;
    // return "redirect:/";
  }

  private String getPropertyValue(@RequestParam("key") String key) {
    String returnValue = "No value";

    String keyValue = env.getProperty(key);

    if (keyValue != null && !keyValue.isEmpty()) {
      returnValue = keyValue;
    }
    return returnValue;
  }
}
