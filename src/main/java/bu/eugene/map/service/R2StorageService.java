package bu.eugene.map.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Service
public class R2StorageService {

    @Value("${access_key}")
    private String accessKey;
    @Value("${secret_key}")
    private String secretKey;


    public String uploadFile(MultipartFile file, String filename) throws IOException {
        S3Client s3Client = S3Client.builder()
                .region(Region.of("auto"))
                .endpointOverride(URI.create("https://f5fdebfc0394dc618f4db780c9bb135f.r2.cloudflarestorage.com"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey,
                                secretKey)
                ))
                .build();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket("diplom")
                .key(filename)
                .contentType(file.getContentType())
                .cacheControl("public, max-age=31536000")
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return "https://pub-71a295251a0341f6a5f6985c64959b7f.r2.dev/" + filename;
    }

    private String generatePublicUrl(String filename) {
        return String.format("https://%s.%s.r2.cloudflarestorage.com/%s",
                "diplom",                              // Имя бакета
                "f5fdebfc0394dc618f4db780c9bb135f",    // Account ID
                filename);
    }
}
