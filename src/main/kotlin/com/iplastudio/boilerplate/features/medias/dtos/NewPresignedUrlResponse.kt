package com.iplastudio.boilerplate.features.medias.dtos

data class NewPresignedUrlResponse(
    val uploadUrl: String,
    val downloadUrl: String
)