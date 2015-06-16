package vivid.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by wujy on 15-6-11.
 */
@Service
public class ResourceService {

    public String calcDigest(byte[] bytes) throws NoSuchAlgorithmException {
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
        return sb.toString();
    }

    public String getFilePath(UUID id, String originalFilename) {
        String[] strs = id.toString().split("-");
        String filename = "uploads";
        for (String str : strs) {
            filename += "/" + str;
        }
        new File(filename).mkdirs();  // create directories recursively
        filename += "/" + originalFilename;
        return filename;
    }

    public void saveFile(byte[] bytes, UUID id, String originalFilename) throws IOException {
        // store the bytes to file
        // File digest format: `{Digest1}-{Digest2}-{Digest3}-{Digest4}`
        // Save file to: `uploads/{Digest1}/{Digest2}/{Digest3}/{Digest4}/{OriginalFilename}`
        Files.write(FileSystems.getDefault().getPath(getFilePath(id, originalFilename)), bytes, StandardOpenOption.CREATE);
    }

}
