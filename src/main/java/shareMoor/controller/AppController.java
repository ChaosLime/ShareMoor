package shareMoor.controller;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shareMoor.services.ApprovalService;
import shareMoor.services.StorageProperties;
import shareMoor.services.StorageService;
import shareMoor.services.StoreUserContactService;
import shareMoor.services.ThumbnailService;

/**
 * Share Moor
 * 
 * AppController.java
 * 
 * Purpose: Controls the actions performed by the webserver as a response to visitor's access the
 * site. This controller was adapted from https://spring.io/guides/gs/uploading-files/ and
 * https://github.com/spring-guides/gs-uploading-files.
 * 
 * @author Mitchell Saunders
 *
 */
@Controller
public class AppController {

  private final StorageService storageService;

  private final StoreUserContactService storeUserContactService;

  private final ThumbnailService thumbnailService;

  private final ApprovalService approvalService;

  /**
   * Constructor for this controller. Initializes an instance of all services used in this file.
   * 
   * @param storageService
   * @param storeUserContactService
   * @param thumbnailService
   * @param approvalService
   */
  @Autowired
  public AppController(StorageService storageService,
      StoreUserContactService storeUserContactService, ThumbnailService thumbnailService,
      ApprovalService approvalService) {
    this.storageService = storageService;
    this.storeUserContactService = storeUserContactService;
    this.thumbnailService = thumbnailService;
    this.approvalService = approvalService;
  }

  /**
   * Default GET mapping that renders all images that the webserver has collected.
   * 
   * @param model Model
   * @return "uploadForm" String HTML template.
   */
  @RequestMapping("/")
  public String listUploadedFiles(Model model, Device device) {

    // System.out.println(device.toString());

    model.addAttribute("files",
        storageService.loadAllFinishedFull()
            .map(path -> MvcUriComponentsBuilder
                .fromMethodName(AppController.class, "serveFile", path.getFileName().toString())
                .build().toUri().toString())
            .collect(Collectors.toList()));
    model.addAttribute("thumbs",
        storageService.loadAllFinishedThumbs()
            .map(path -> MvcUriComponentsBuilder
                .fromMethodName(AppController.class, "serveThumb", path.getFileName().toString())
                .build().toUri().toString())
            .collect(Collectors.toList()));

    String htmlPage = "";
    if (device.isMobile() || device.isTablet()) {
      htmlPage = "uploadForm/mobile";
    } else if (device.isNormal()) {
      htmlPage = "uploadForm/normal";
    } else {
      htmlPage = "uploadForm/normal";
    }
    return htmlPage;

  }

  /**
   * GET mapping for accessing approval webpage. This will load all images to the administrator for
   * review.
   * 
   * @param model Model
   * @return "approvalForm" String HTML template.
   */
  @GetMapping("/approval")
  public String listFiles(Model model, Device device) {

    model.addAttribute("reviewFiles",
        storageService.loadAllReviewFull().map(path -> MvcUriComponentsBuilder
            .fromMethodName(AppController.class, "serveReviewFile", path.getFileName().toString())
            .build().toUri().toString()).collect(Collectors.toList()));
    model.addAttribute("reviewThumbs",
        storageService.loadAllReviewThumbs().map(path -> MvcUriComponentsBuilder
            .fromMethodName(AppController.class, "serveReviewThumb", path.getFileName().toString())
            .build().toUri().toString()).collect(Collectors.toList()));

    String htmlPage = "";
    if (device.isMobile() || device.isTablet()) {
      htmlPage = "approvalForm/mobile";
    } else if (device.isNormal()) {
      htmlPage = "approvalForm/normal";
    } else {
      htmlPage = "approvalForm/normal";
    }
    return htmlPage;

  }

  /**
   * GET mapping that allows visitors to download a specific file that they've clicked on.
   * 
   * @param filename String that represents the full filename of the file they wish to download.
   * @return file ResponseEntity<Resource> is the file that the user has asked to download.
   */
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceFinishedFull(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  /**
   * GET mapping that allows visitors to preview all of the photos that are available to be
   * downloaded. This will be called once for each item on the webserver that is ready for download.
   * 
   * @param filename String represents the filename of a thumbnail that the user has requested to
   *        see.
   * @return file ResponseEntity<Resource> is the thumbnail image that represents each of the files
   *         ready to be downloaded.
   */

  @GetMapping("/thumbs/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveThumb(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceFinishedThumbs(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  /**
   * GET mapping that will perform the same function as serveFile does, except for the purposes of
   * allowing the reviewer download a file to preview it more closely.
   * 
   * @param filename String represents the filename of the full file that is in the review folder
   *        that the user has requested to download.
   * @return file ReponseEntity<Resource> is the file that the administrator has asked to download.
   */
  @GetMapping("/reviewFiles/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveReviewFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceReviewFull(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  /**
   * GET mapping that will perform the same function as the serverThumb does, except for the
   * purposes of allowing the reviewer to see all of the thumbnails that represent the files that
   * need to be reviewed.
   * 
   * @param filename String represents the file that the user wants to see on the webpage.
   * @return file ResponseEntity<Resource> is the file that the use has asked to preview.
   */
  @GetMapping("/reviewThumbs/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveReviewThumb(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceReviewThumbs(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @GetMapping("/assests/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveAssest(@PathVariable String filename) {

    Resource file = storageService.loadAsResourceAssest(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }


  @GetMapping("/share")
  public String displayQRcodes(Model model, Device device) {

    // TODO: remove, used for testing Exiftool. Be sure to abstract to a service.
    String assestDir = StorageProperties.getAssestsLocation().toString() + File.separator;
    String file = "1.jpg";
    String filePath = "/home/nick/demo/";
    String status = "public";
    // CrossPlatformTools.setUpExifToolCall(assestDir, filePath, file, status);

    // System.out.println(device.toString());
    String htmlPage = "";
    if (device.isMobile() || device.isTablet()) {
      htmlPage = "share/mobile";
    } else if (device.isNormal()) {
      htmlPage = "share/normal";
    } else {
      htmlPage = "share/normal";
    }
    return htmlPage;

  }

  @GetMapping("/about")
  public String displayAbout(Model model, Device device) {

    // System.out.println(device.toString());
    String htmlPage = "";
    if (device.isMobile() || device.isTablet()) {
      htmlPage = "about/mobile";
    } else if (device.isNormal()) {
      htmlPage = "about/normal";
    } else {
      htmlPage = "about/normal";
    }
    return htmlPage;

  }

  @GetMapping("/welcome")
  public String displayWelcome(Model model, Device device) {

    // System.out.println(device.toString());
    String htmlPage = "";
    if (device.isMobile() || device.isTablet()) {
      htmlPage = "welcome/mobile";
    } else if (device.isNormal()) {
      htmlPage = "welcome/normal";
    } else {
      htmlPage = "welcome/normal";
    }
    return htmlPage;

  }

  /**
   * POST mapping that facilitates the storage of voluntarily provided contact information.
   * 
   * @param model Model
   * @param contactInfo String holds the contact information provided by the user
   * @return listUploadedFiles String which will use the defined GET mapping to show all of the
   *         files collected.
   */
  @PostMapping("/contactInfo")
  public String collectContactInfo(Model model, Device device,
      @RequestParam("contactInfo") String contactInfo) {

    storeUserContactService.writeContactInfo(contactInfo);

    model.addAttribute("message", "Thank you for providing your contact information!");

    return listUploadedFiles(model, device);
  }

  /**
   * POST mapping that will facilitate file uploading from the user. Code was taken and repurposed
   * from https://stackoverflow.com/questions/25699727/multipart-file-upload-spring-boot to make
   * this mapping handle multiple files at the same time.
   * 
   * @param file MultipartFile[] which will hold a list of files provided by the user.
   * @param redirectAttributes RedirectAttributes provide useful error messages back to the user
   *        about their attempted file upload.
   * @return "redirect:/" String which sends the user back to the base GET mapping.
   */
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
        if (storedFileLocation != null) {
          System.out.println(storedFileLocation);
          thumbnailService.createThumbnail(storedFileLocation);
        }

      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      }

    }

    redirectAttributes.addFlashAttribute("message", successMessage);

    return "redirect:/";
  }

  /**
   * POST mapping that facilitates the approval or denial of a file upload by the administrator.
   * This uses the ApprovalService.
   * 
   * @param model Model
   * @param approvedFilename Optional<String> which may hold the filename of the file that the
   *        administrator has approved.
   * @param deniedFilename Optional<String> which may hold the filename of the file that the
   *        administrator has denied.
   * @return "redirect:/approval" String which will locate the administrator back at the approval
   *         page where they can continue to approve and deny other files.
   */
  @PostMapping("/approval")
  public String approveFile(Model model,
      @RequestParam("approvedFilename") Optional<String> approvedFilename,
      @RequestParam("deniedFilename") Optional<String> deniedFilename) {

    if (approvedFilename.isPresent()) {
      System.out.println("Approved:" + approvedFilename);
      approvalService.saveFileInFinishedFolder(approvedFilename.toString());
      model.addAttribute("message", "File approved.");
    } else if (deniedFilename.isPresent()) {
      System.out.println("Denied:" + deniedFilename);
      approvalService.saveFilesInDeniedFolder(deniedFilename.toString());
      model.addAttribute("message", "File denied.");
    }

    return "redirect:/approval";
  }

}
