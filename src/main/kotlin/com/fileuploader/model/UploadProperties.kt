package com.fileuploader.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "upload")
class UploadProperties {
    lateinit var location: String
}