package vivid.controller;

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

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;

/**
 * Created by wujy on 15-6-4.
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    private ResourceRepository resourceRepository;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String formUpload() {
        //SecurityUtils.getSubject().checkPermission("UPLOAD");
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
        //SecurityUtils.getSubject().checkPermission("UPLOAD");
        try {
            if (file.isEmpty()) {
                throw new FileUploadException("The file is empty.");
            }
            byte[] bytes = file.getBytes();

            // calculate the file checksum
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            byte digestByteData[] = md.digest();

            //convert the byte to hex format
            StringBuilder sb = new StringBuilder();
            for (byte byteData : digestByteData) {
                sb.append(Integer.toString((byteData & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println("File digest Hex format: " + sb.toString());
            String digest = sb.toString();

            // metadata persistence
            Resource resource = resourceRepository.findByDigest(digest);
            if (resource == null) {
                resource = new Resource(file.getSize(), digest, file.getName(), file.getOriginalFilename());
            }
            resource = resourceRepository.save(resource);
            System.out.println("Resource ID: " + resource.getId());

            // store the bytes to file
            String[] strs = resource.getId().toString().split("-");
            String filename = "uploads";
            for (String str : strs) {
                filename += "/" + str;
            }
            new File(filename).mkdirs();  // create directories recursively
            filename += "/" + file.getOriginalFilename();
            // File digest format: `{Digest1}-{Digest2}-{Digest3}-{Digest4}`
            // Save file to: `uploads/{Digest1}/{Digest2}/{Digest3}/{Digest4}/{OriginalFilename}`
            Files.write(FileSystems.getDefault().getPath(filename), bytes, StandardOpenOption.CREATE);

            return "resources/uploadSuccess";
        } catch (Exception e) {
            e.printStackTrace();
            return "resources/uploadFailure";
        }
    }

}
