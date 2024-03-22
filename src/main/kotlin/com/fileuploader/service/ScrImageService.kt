package com.fileuploader.service

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.webp.WebpWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException

@Service
class ScrImageService {

    private val logger: Logger = LoggerFactory.getLogger(UploadService::class.java)

    @Async
    fun makeThumbnail(path: String, filename: String, extension: String) {
        try {
            val imageFile: ImmutableImage = ImmutableImage.loader().fromFile(path)
            //webp format only support max width and height 16383
            val dimension: Dimension = calculateDimension(imageFile.height, imageFile.width)
            imageFile.scaleTo(dimension.width, dimension.height)
            imageFile.output(WebpWriter.DEFAULT, File(path.replace(extension, "webp")))

        } catch (e: IOException) {
            logger.error("convert to webp error file $filename with error ${e.message}")
        }
    }

    private fun calculateDimension(
        height: Int,
        width: Int,
    ): Dimension {
        var newHeight = height
        var newWidth = width
        if (newHeight > 16383) {
            newHeight = 16383
            newWidth = (width * newHeight) / height
        }
        if (newWidth > 16383) {
            newWidth = 16383
            newHeight = (height * newWidth) / width
        }
        return Dimension(newWidth, newHeight)
    }

    inner class Dimension(val width: Int, val height: Int)
}