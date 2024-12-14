package com.iplastudio.boilerplate.features.medias

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppBucketConfig(
    private val mediaProperties: MediaProperties,
) {

    @Bean
    fun appS3Bucket() = S3BucketService(mediaProperties.s3.buckets.first())

}