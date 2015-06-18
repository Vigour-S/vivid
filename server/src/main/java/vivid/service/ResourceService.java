package vivid.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vivid.support.HttpClientUtil;
import vivid.support.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
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

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
    private static final int REMOTE_RETRY_TIMES = 3;
    private static final int REMOTE_SOCKET_TIMEOUT = 30;
    private static final int REMOTE_CONNECTION_TIMEOUT = 30;

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
        logger.info("File digest Hex format: {}", sb.toString());
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

    public MultipartFile downloadFile(String url) {
        CloseableHttpClient httpClient = HttpClientUtil.get(REMOTE_RETRY_TIMES, REMOTE_SOCKET_TIMEOUT, REMOTE_CONNECTION_TIMEOUT, new BasicCredentialsProvider());
        CloseableHttpResponse httpResponse = null;
        HttpGet httpGet = new HttpGet(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            httpEntity.writeTo(baos);
            logger.info("Downloaded '{}' type '{}'", url, httpEntity.getContentType().getValue());
            return new MockMultipartFile("file", "remote", httpEntity.getContentType().getValue(), baos.toByteArray());
        } catch (Exception e) {
            logger.error(null, e);
            throw new ResourceNotFoundException(e.getMessage(), e.getCause());
        } finally {
            try {
                baos.close();
                if (null != httpResponse) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                logger.error(null, e);
            }
            httpGet.releaseConnection();
        }
    }

}
