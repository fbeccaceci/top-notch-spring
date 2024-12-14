package com.iplastudio.boilerplate.features.medias

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.medias")
data class MediaProperties(
    val s3: S3Properties,
    val validation: MediaValidationProperties
)

@ConfigurationProperties("app.medias.s3")
data class S3Properties(
    val buckets: List<AppS3Bucket>
)

data class AppS3Bucket(
    val name: String,
    val region: String
)

@ConfigurationProperties("app.medias.validation")
data class MediaValidationProperties(
    val maxFileSize: Long,
)