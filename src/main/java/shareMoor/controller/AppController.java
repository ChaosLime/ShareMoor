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
  
  private final ThumbnailService thumbnailService;
  
  private final ApprovalService approvalService;

  @Autowired
  public AppController(StorageService storageService, 
                       StoreUserContactService storeUserContactService,
                       ThumbnailService thumbnailService,
                       ApprovalService approvalService) {
    this.storageService = storageService;
    this.storeUserContactService = storeUserContactService;
    this.thumbnailService = thumbnailService;
    this.approvalService = approvalService;
  }

  @GetMapping("/")
  public String listUploadedFiles(Model model) throws IOException {

    model.addAttribute("files", storageService.loadAllFull().map(
        path -> MvcUriComponentsBuilder.fromMethodName(AppController.class,
            "serveFile", path.getFileName().toString()).build().toUri().toString())
        .collect(Collectors.toList()));
    model.addAttribute("thumbs", storageService.loadAllThumbs().map(
        path -> MvcUriComponentsBuilder.fromMethodName(AppController.class,
            "serveThumb", path.getFileName().toString()).build().toUri().toString())
        .collect(Collectors.toList()));

    return "uploadForm";
  }

  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceFull(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  @GetMapping("/thumbs/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveThumb(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceThumbs(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  @PostMapping("/contactInfo")
  public String collectContactInfo(Model model, @RequestParam("contactInfo") String contactInfo) {
   
    storeUserContactService.writeContactInfo(contactInfo);
    
    model.addAttribute("files", storageService.loadAllFull().map(
        path -> MvcUriComponentsBuilder.fromMethodName(AppController.class,
            "serveFile", path.getFileName().toString()).build().toUri().toString())
        .collect(Collectors.toList()));
    model.addAttribute("thumbs", storageService.loadAllThumbs().map(
        path -> MvcUriComponentsBuilder.fromMethodName(AppController.class,
            "serveThumb", path.getFileName().toString()).build().toUri().toString())
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
        // This service call will result in a file stored in the staging folder.
        // The file will be given a sequential name to ensure that it doesn't
        // conflict with another file. TODO: Check for highest file number on start.
        String storedFileLocation = storageService.store(file[i]);
        System.out.println(storedFileLocation);
        
        thumbnailService.createThumbnail(storedFileLocation);
        
        // TODO: Move this to its own mapping at a later date.
        approvalService.saveFileInFinishedFolder(storedFileLocation);
        
      } catch(Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      }
      //successMessage += "You successfully uploaded " + file[i].getOriginalFilename() + "!<br />";
    }
    
    redirectAttributes.addFlashAttribute("message", successMessage);

    return "redirect:/";
  }

}
