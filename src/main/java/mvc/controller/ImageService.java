package mvc.controller;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import mvc.amazon.AmazonS3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Component
public class ImageService {
    @Autowired
    private AmazonS3ClientService clientService;

    public InputStream getImage(String imageType, Integer imgNum) {
        String bucketName = clientService.getBucketName();
        String key = imageType + "/" + imgNum + ".png";
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        S3Object object = clientService.client.getObject(getObjectRequest);

        return new BufferedInputStream(object.getObjectContent());
    }

    public Integer getImagesCount(String imageType) {
        ObjectListing listObjects = clientService.client.listObjects(clientService.getBucketName());
        Integer count = 0;
        String foundName = imageType + "/";
        for (S3ObjectSummary objectSummary : listObjects.getObjectSummaries()) {
            if (objectSummary.getKey().contains(imageType + "/") && objectSummary.getKey().length() > foundName.length()) {
                ++count;
            }
        }
        return count;
    }
}
