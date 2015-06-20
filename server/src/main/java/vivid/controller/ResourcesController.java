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
import java.io.InputStream;
import java.util.*;

/**
 * Created by wujy on 15-6-4.
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);

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

    @Transactional
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    Map handleFormUpload(@RequestParam MultipartFile file) {
        SecurityUtils.getSubject().checkRole("USER");
        try {
            if (file.isEmpty()) {
                throw new FileUploadException("The file is empty.");
            }

            // metadata persistence
            String digest = resourceService.calcDigest(file.getBytes());
            Resource resource = resourceRepository.findByDigest(digest);
            if (resource == null) {
                resource = new Resource(file.getSize(), digest, file.getOriginalFilename(), file.getOriginalFilename(), file.getContentType());
            }
            resource = resourceRepository.save(resource);
            logger.info("Resource ID: '{}'", resource.getId());

            // save to filesystem
            resourceService.saveFile(file.getBytes(), resource.getId(), file.getOriginalFilename());

            // save pins to timeline
            resource.setUrl("/resources/view/" + resource.getId());
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

    @Transactional
    @RequestMapping(value = "/upload_by_url", method = RequestMethod.POST)
    public
    @ResponseBody
    Map handleFormFetchRemote(@RequestParam String url) {
        SecurityUtils.getSubject().checkRole("USER");
        try {
            if (url.isEmpty()) {
                throw new FileUploadException("The url is invalid.");
            }

            // download remote file and mock it as a MultipartFile uploaded
            MultipartFile file = resourceService.downloadFile(url);

            // metadata persistence
            String digest = resourceService.calcDigest(file.getBytes());
            Resource resource = resourceRepository.findByDigest(digest);
            if (resource == null) {
                resource = new Resource(file.getSize(), digest, file.getOriginalFilename(), file.getOriginalFilename(), file.getContentType());
            }
            resource = resourceRepository.save(resource);
            logger.info("Resource ID: '{}'", resource.getId());

            // save to filesystem
            resourceService.saveFile(file.getBytes(), resource.getId(), file.getOriginalFilename());

            // save pins to timeline
            resource.setUrl("/resources/view/" + resource.getId());
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
            logger.error("Could not show picture " + id, e);
            throw new ResourceNotFoundException(e.getMessage(), e.getCause());
        }
    }

}
