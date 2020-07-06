package mvc.amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class AmazonS3ClientService extends AmazonS3Client {
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;
    public AmazonS3Client client;

    public AmazonS3ClientService() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            accessKeyId = properties.getProperty("accessKeyId");
            secretAccessKey = properties.getProperty("secretAccessKey");
            bucketName = properties.getProperty("bucketName");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        client = new AmazonS3Client(awsCredentials);
    }

    public String getBucketName() {
        return bucketName;
    }
}
