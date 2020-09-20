//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files
package exception;

public class StorageException extends RuntimeException {

  public StorageException(String message) {
    super(message);
  }

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}