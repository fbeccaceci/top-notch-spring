package com.iplastudio.boilerplate.features.medias

import com.iplastudio.boilerplate.features.medias.dtos.NewPresignedUrlRequest
import com.iplastudio.boilerplate.features.medias.dtos.NewPresignedUrlResponse
import com.iplastudio.boilerplate.properties.SelfProperties
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Medias")
@RestController
@RequestMapping("/medias")
class MediaController(
    private val mediaProperties: MediaProperties,
    private val s3BucketService: S3BucketService,
    private val selfProperties: SelfProperties
) {

    @PostMapping("/presigned-url")
    fun newPresignedUrl(@RequestBody @Valid req: NewPresignedUrlRequest): NewPresignedUrlResponse {
        if (req.contentLength > mediaProperties.validation.maxFileSize) {
            throw MediaValidationException("File size is too big")
        }

        val key = UUID.randomUUID().toString()

        val uploadUrl = s3BucketService.getPreSignedUploadUrl { builder ->
            builder.key(key)
            builder.contentType(req.contentType)
            builder.contentLength(req.contentLength)
        }

        val downloadUrl = selfProperties.baseUrl + "/medias/view/" +  key

        return NewPresignedUrlResponse(uploadUrl, downloadUrl)
    }

    @Hidden
    @GetMapping("/view/{fileName}")
    fun downloadFile(
        @PathVariable fileName: String,
        response: HttpServletResponse
    ) {
        val objectStream = s3BucketService.getMediaStream(fileName)

        response.contentType = objectStream.response().contentType()

        objectStream.copyTo(response.outputStream)
    }

}