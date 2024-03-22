package com.fileuploader.exception

class UploadFileNotFoundException(
    override val message: String
): RuntimeException()