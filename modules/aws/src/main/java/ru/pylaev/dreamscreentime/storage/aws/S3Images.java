package ru.pylaev.dreamscreentime.storage.aws;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import ru.pylaev.dreamscreentime.ImagesRepository;
import ru.pylaev.dreamscreentime.storage.aws.exceptions.FailedDownloadingImageException;
import ru.pylaev.dreamscreentime.storage.aws.exceptions.FailedGetListOfObjectsException;

import java.io.BufferedInputStream;
import java.io.InputStream;

@CacheConfig(cacheNames = {"Images"})
@Slf4j
public class S3Images implements ImagesRepository {

    public static final String CATS = "cats";
    public static final String TIMES = "times";

    private final AmazonS3Client awsClient;
    private final String bucketName;

    public S3Images(AmazonS3Client awsClient, AwsConfigurationProperties configurationProperties) {
        this.awsClient = awsClient;
        this.bucketName = configurationProperties.bucketName();
    }

    @Override
    public InputStream getCat(Integer index) {
        return getImage(CATS, index);
    }

    @Override
    @Cacheable(value = "Count", key = "T(ru.pylaev.dreamscreentime.storage.aws.S3Images).CATS")
    public Integer getCatsCount() {
        return getImagesCountInFolder(CATS);
    }


    @Override
    public InputStream getTime(Integer index) {
        return getImage(TIMES, index);
    }

    @Override
    @Cacheable(value = "Count", key = "T(ru.pylaev.dreamscreentime.storage.aws.S3Images).TIMES")
    public Integer getDreamTimesCount() {
        return getImagesCountInFolder(TIMES);
    }

    protected InputStream getImage(String imageType, Integer imgNum) {
        String key = buildImagePath(imageType, imgNum);
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        try {
            S3Object object = awsClient.getObject(getObjectRequest);
            return new BufferedInputStream(object.getObjectContent());
        } catch (SdkClientException e) {
            log.warn("Failed to download image by key {} from S3", key);
            throw new FailedDownloadingImageException(e);
        }
    }

    protected Integer getImagesCountInFolder(String folderName) {
        String path = folderName + "/";
        try {
            ObjectListing listObjects = awsClient.listObjects(bucketName);
            return listObjects.getObjectSummaries()
                    .stream()
                    .filter(objSum -> objSum.getKey().contains(path))
                    .filter(objSum -> objSum.getKey().length() > path.length())
                    .toList()
                    .size();
        } catch (SdkClientException e){
            log.warn("Failed to get list of objects from bucket {}", path);
            throw new FailedGetListOfObjectsException(e);
        }
    }

    protected String buildImagePath(String folder, Integer num) {
        return folder + "/" + num + ".png";
    }
}
