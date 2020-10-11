// https://spring.io/guides/gs/uploading-files/
// https://github.com/spring-guides/gs-uploading-files
package shareMoor.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shareMoor.exception.StorageFileNotFoundException;
import shareMoor.services.StorageService;

@AutoConfigureMockMvc
@SpringBootTest // (classes = UploadingFilesApplication.class)
public class FileUploadTests {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private StorageService storageService;

  /*
   * @Test public void shouldListAllFiles() throws Exception {
   * given(this.storageService.loadAllFinishedFull()) .willReturn(Stream.of(Paths.get("first.txt"),
   * Paths.get("second.txt")));
   * 
   * this.mvc.perform(get("/")).andExpect(status().isOk()) .andExpect(model().attribute("files",
   * Matchers.contains("http://localhost/files/first.txt", "http://localhost/files/second.txt"))); }
   */
  /*
   * @Test public void shouldSaveUploadedFile() throws Exception { MockMultipartFile multipartFile =
   * new MockMultipartFile("file", "test.txt", "text/plain", "Spring Framework".getBytes());
   * this.mvc.perform(multipart("/").file(multipartFile)).andExpect(status().isFound())
   * .andExpect(header().string("Location", "/"));
   * 
   * then(this.storageService).should().store(multipartFile); }
   */
  @Test
  public void should404WhenMissingFile() throws Exception {
    given(this.storageService.loadAsResourceFinishedFull("test.png"))
        .willThrow(StorageFileNotFoundException.class);
    this.mvc.perform(get("/file/test.png")).andExpect(status().isNotFound());
  }

}
