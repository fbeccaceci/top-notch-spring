package com.iplastudio.boilerplate.features.medias.dtos

data class NewPresignedUrlRequest(
    val fileName: String,
    val contentType: String,
    val contentLength: Long
)
