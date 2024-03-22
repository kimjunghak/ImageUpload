package com.fileuploader.service

import com.fileuploader.exception.UploadException
import com.fileuploader.model.UploadProperties
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils.cleanPath
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

@Service
class UploadService(
    private val scrImageService: ScrImageService,
    properties: UploadProperties,
) {

    private val location: Path = Path.of(properties.location)

    fun init() {
        if (!Files.exists(location)) {
            Files.createDirectories(location)
        }
    }

    fun upload(multipartFile: MultipartFile) {
        val filename = cleanPath(multipartFile.originalFilename!!)
        try {
            if (multipartFile.isEmpty) {
                throw UploadException("Failed to store empty file $filename")
            }

            if (multipartFile.contentType?.contains("image") == false) {
                throw UploadException("Failed to store file $filename, not an image")
            }
            multipartFile.inputStream.use { inputStream ->
                val path = this.location.resolve(filename)
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING)
                // make thumbnail using scrimage library
                scrImageService.makeThumbnail(path.toString(), filename, FilenameUtils.getExtension(filename))
            }
        } catch (e: IOException) {
            throw UploadException("Failed to store file $filename")
        }
    }

    fun load(filename: String): String {
        return location.resolve(filename).toString()
    }

    fun deleteAll() {
        FileSystemUtils.deleteRecursively(location.toFile())
    }

    fun loadAll(): Stream<String> {
        try {
            return Files.walk(this.location, 1)
                .filter { path: Path -> path != location }
                .map { path: Path -> path.toString() }
        } catch (e: IOException) {
            throw UploadException("Failed to read stored files")
        }
    }
}

