package shareMoor.controller;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shareMoor.domain.ConfigHandler;
import shareMoor.services.ApprovalService;
import shareMoor.services.ExifMetaDataService;
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

  private final ExifMetaDataService exifService;

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
      ApprovalService approvalService, ExifMetaDataService exifService) {
    this.storageService = storageService;
    this.storeUserContactService = storeUserContactService;
    this.thumbnailService = thumbnailService;
    this.approvalService = approvalService;
    this.exifService = exifService;
  }

  @GetMapping("/")
  public String displayWelcome(Model model, Device device) {
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
   * Default GET mapping that renders all images that the webserver has collected.
   * 
   * @param model Model
   * @return "uploadForm" String HTML template.
   */
  @RequestMapping("/upload")
  public String uploadFiles(Model model, Device device) {
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

  @RequestMapping("/gallery")
  public String listUploadedFiles(Model model, Device device) {
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
      htmlPage = "gallery/mobile";
    } else if (device.isNormal()) {
      htmlPage = "gallery/normal";
    } else {
      htmlPage = "gallery/normal";
    }
    return htmlPage;
  }

  @RequestMapping(path = {"/pageView"}, method = RequestMethod.GET)
  public String listUploadedFilesFirstPage(Model model, Device device) {

    model.addAttribute("pageNumber", 1);
    
    model.addAttribute("pageSize", 5);

    Optional<Integer> pageNum = Optional.ofNullable(1);
    
    Optional<Integer> pageSize = Optional.ofNullable(5);

    return listUploadedFilesPageView(model, device, pageNum, pageSize);
  }

  @PostMapping("/pageView/page")
  public String listUploadedFilesPageView(Model model, Device device,
      @RequestParam("pageNumber") Optional<Integer> pageNumber,
      @RequestParam("pageSize") Optional<Integer> pageSize) {
    
    model.addAttribute("files",
        storageService.loadAllFinishedFull()
            .map(path -> MvcUriComponentsBuilder
                .fromMethodName(AppController.class, "serveFile", path.getFileName().toString())
                .build().toUri().toString())
            .collect(Collectors.toList()));
    
    if (pageNumber.isPresent()) {
      model.addAttribute("pageNumber", pageNumber.get());
    } else {
      model.addAttribute("pageNumber", 1);
    }
    
    if (pageSize.isPresent()) {
      model.addAttribute("pageSize", pageSize.get());
      int numberOfFiles = storageService.countFilesInFinishedFullDir();
      model.addAttribute("maxPageNumber", Math.ceil((double) numberOfFiles / (double)pageSize.get()));
    } else {
      model.addAttribute("pageSize", 5);
    }

    String htmlPage = "";
    if (device.isMobile() || device.isTablet()) {
      htmlPage = "pageView/mobile";
    } else if (device.isNormal()) {
      htmlPage = "pageView/normal";
    } else {
      htmlPage = "pageView/normal";
    }
    return htmlPage;
  }

  @RequestMapping("/contact")
  public String contactPage(Model model, Device device) {
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
      htmlPage = "contact/mobile";
    } else if (device.isNormal()) {
      htmlPage = "contact/normal";
    } else {
      htmlPage = "contact/normal";
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

    MediaType mediaType = MediaType.valueOf(storageService.getMimeType(filename));

    Resource file = storageService.loadAsResourceFinishedFull(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"")
        .contentType(mediaType).body(file);
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

    MediaType mediaType = MediaType.valueOf(storageService.getMimeType(filename));

    Resource file = storageService.loadAsResourceReviewFull(filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"")
        .contentType(mediaType).body(file);
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
    String uri = ConfigHandler.getFullAddress();
    model.addAttribute("address", uri);

    String SSID = ConfigHandler.getSSID();
    model.addAttribute("ssid", SSID);
    String pass = ConfigHandler.getWifiPass();
    model.addAttribute("pass", pass);

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
      @RequestParam("contactInfo") Optional<String> contactInfo) {

    if (contactInfo.isPresent()) {
      storeUserContactService.writeContactInfo(contactInfo.get());
      model.addAttribute("message", "Thank you for providing your contact information!");
    } else {
      model.addAttribute("message", "Cannot submit an empty email address. Please try again.");
    }

    return contactPage(model, device);
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
  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile[] file,
      RedirectAttributes redirectAttributes) {

    String message = "You successfully uploaded your selected files!";
    // TODO: be sure that the 'status' here will be website adjustable, defaulting to public for
    // testing.
    String status = "public";

    boolean approveAll = Boolean.valueOf(ConfigHandler.getSettingsValue("autoApprove"));

    // Checks the case where only one object exists, where it is empty, and if so throws and error.

    String result = storageService.checkIfEmpty(file[0]);
    if (result != null) {
      message = "Upload failed. You can not upload nothing.";
      redirectAttributes.addFlashAttribute("message", message);
      return "redirect:/upload";
    }

    for (int i = 0; i < file.length; i++) {
      try {
        // This service call will result in a file stored in the staging folder.
        // The file will be given a sequential name to ensure that it doesn't
        // conflict with another file. TODO: Check for highest file number on start.
        String storedFileLocation = storageService.store(file[i]);
        if (storedFileLocation != null) {
          System.out.println(storedFileLocation);
          exifService.scrubFile(storedFileLocation, status);
          thumbnailService.createThumbnail(storedFileLocation);

          if (approveAll) {
            System.out.println("Approved:" + storedFileLocation);
            approvalService.saveFileInFinishedFolder(storedFileLocation);
          }

        } else {
          message =
              "Uploaded Failed, please try again. If error continues to occur, a file may not be supported.";
        }

      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      }
    }

    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/upload";
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
