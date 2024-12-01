package com.iplastudio.boilerplate.features.medias

import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration


class S3BucketService(bucketProperties: AppS3Bucket) {

    private val bucketName = bucketProperties.name
    private val s3Client: S3Client
    private val s3PreSigner: S3Presigner

    init {
        val region = Region.of(bucketProperties.region)
        s3Client = S3Client.builder().region(region).build()

        s3PreSigner = S3Presigner.builder().region(region).build()
    }

    /**
     * Get pre-signed upload URL for the media, use this method to upload the media to S3 bucket
     * after checking if the user is authorized to upload the media
     */
    fun getPreSignedUploadUrl(fileName: String, metadata: Map<String?, String?> = emptyMap()): String {
        val objectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .metadata(metadata)
            .build()

        val preSignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(objectRequest)
            .build()

        val preSignedRequest = s3PreSigner.presignPutObject(preSignRequest)
        return preSignedRequest.url().toExternalForm()
    }

    /**
     * Get media stream from S3 bucket, use this method to stream the media to the client
     * after checking if the user is authorized to access the media
     */
    fun getMediaStream(fileName: String): ResponseInputStream<GetObjectResponse> {
        val req = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build()

        return s3Client.getObject(req)
    }

}