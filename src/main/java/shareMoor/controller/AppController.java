//https://spring.io/guides/gs/uploading-files/
//https://github.com/spring-guides/gs-uploading-files
package shareMoor.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shareMoor.services.*;

@Controller
public class AppController {
  
  private final StorageService storageService;
  
  private final StoreUserContactService storeUserContactService;
  
 // private final ThumbnailService thumbnailService;

  @Autowired
  public AppController(StorageService storageService, 
                       StoreUserContactService storeUserContactService) {//,
                       //ThumbnailService thumbnailService) {
    this.storageService = storageService;
    this.storeUserContactService = storeUserContactService;
    //this.thumbnailService = thumbnailService;
  }

  @GetMapping("/")
  public String listUploadedFiles(Model model) throws IOException {

    model.addAttribute("files", storageService.loadAll().map(
        path -> MvcUriComponentsBuilder.fromMethodName(AppController.class,
            "serveFile", path.getFileName().toString()).build().toUri().toString())
        .collect(Collectors.toList()));

    return "uploadForm";
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  @PostMapping("/contactInfo")
  public String collectContactInfo(Model model, @RequestParam("contactInfo") String contactInfo) {
   
    storeUserContactService.writeContactInfo(contactInfo);
    
    model.addAttribute("files", storageService.loadAll().map(
        path -> MvcUriComponentsBuilder.fromMethodName(AppController.class,
            "serveFile", path.getFileName().toString()).build().toUri().toString())
        .collect(Collectors.toList()));
    model.addAttribute("message", "Thank you for providing your contact information!");
    
    return "uploadForm";
  }

  // To make this multiple file friendly, I'm following this forum: https://stackoverflow.com/questions/25699727/multipart-file-upload-spring-boot
  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file") MultipartFile[] file,
      RedirectAttributes redirectAttributes) {

    String successMessage = "You successfully uploaded your selected files!";
    
    for (int i = 0; i < file.length; i++) {
      try {
        storageService.store(file[i]);
        //thumbnailService.saveThumbnail(file[i].getOriginalFilename());
      } catch(Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      }
      //successMessage += "You successfully uploaded " + file[i].getOriginalFilename() + "!<br />";
    }
    
    redirectAttributes.addFlashAttribute("message", successMessage);

    return "redirect:/";
  }

}
