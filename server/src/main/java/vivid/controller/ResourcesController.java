package vivid.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vivid.entity.Resource;
import vivid.repository.ResourceRepository;
import vivid.service.ResourceService;

/**
 * Created by wujy on 15-6-4.
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String formUpload() {
        SecurityUtils.getSubject().checkPermission("UPLOAD");
        return "resources/upload";
    }

    @RequestMapping(value = "/uploadSuccess", method = RequestMethod.GET)
    public String uploadSuccess() {
        return "resources/uploadSuccess";
    }

    @RequestMapping(value = "/uploadFailure", method = RequestMethod.GET)
    public String uploadFailure() {
        return "resources/uploadFailure";
    }

    @Transactional
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam MultipartFile file) {
        SecurityUtils.getSubject().checkPermission("UPLOAD");
        try {
            if (file.isEmpty()) {
                throw new FileUploadException("The file is empty.");
            }

            String digest = resourceService.calcDigest(file.getBytes());

            // metadata persistence
            Resource resource = resourceRepository.findByDigest(digest);
            if (resource == null) {
                resource = new Resource(file.getSize(), digest, file.getName(), file.getOriginalFilename());
            }
            resource = resourceRepository.save(resource);
            System.out.println("Resource ID: " + resource.getId());

            resourceService.saveFile(file.getBytes(), resource.getId(), file.getOriginalFilename());

            return "resources/uploadSuccess";
        } catch (Exception e) {
            e.printStackTrace();
            return "resources/uploadFailure";
        }
    }

}
