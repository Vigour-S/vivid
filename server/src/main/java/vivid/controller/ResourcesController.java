package vivid.controller;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vivid.entity.Resource;
import vivid.repository.ResourceRepository;
import vivid.service.FeedService;
import vivid.service.ResourceService;
import vivid.support.ResourceNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by wujy on 15-6-4.
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    private static final Logger log = LoggerFactory.getLogger(ResourcesController.class);

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private FeedService feedService;

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
    public
    @ResponseBody
    Map handleFormUpload(@RequestParam MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new FileUploadException("The file is empty.");
            }

            String digest = resourceService.calcDigest(file.getBytes());

            // metadata persistence
            Resource resource = resourceRepository.findByDigest(digest);
            if (resource == null) {
                resource = new Resource(file.getSize(), digest, file.getOriginalFilename(), file.getOriginalFilename(), file.getContentType());
            }
            resource = resourceRepository.save(resource);
            System.out.println("Resource ID: " + resource.getId());

            resourceService.saveFile(file.getBytes(), resource.getId(), file.getOriginalFilename());

            resource.setUrl("/resources/view/" + resource.getId());

            // save pins to timeline
            feedService.saveResourcePin((String) SecurityUtils.getSubject().getPrincipal(), resource.getUrl());

            List<Resource> list = new LinkedList<Resource>();
            list.add(resource);
            Map<String, Object> files = new HashMap<String, Object>();
            files.put("files", list);
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public void picture(HttpServletResponse response, @PathVariable UUID id) {
        try {
            Resource resource = resourceRepository.findOne(id);
            File imageFile = new File(resourceService.getFilePath(resource.getId(), resource.getName()));
            response.setContentType(resource.getContentType());
            response.setContentLength(resource.getSize().intValue());
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            log.error("Could not show picture " + id, e);
            throw new ResourceNotFoundException(e.getMessage(), e.getCause());
        }
    }

}
