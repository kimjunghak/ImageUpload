package com.fileuploader.service

import com.fileuploader.exception.UploadException
import com.fileuploader.model.UploadProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.mock
import org.springframework.mock.web.MockMultipartFile

class UploadServiceTest {

    private lateinit var uploadService: UploadService

    private val scrImageService = mock(ScrImageService::class.java)

    @BeforeEach
    fun setUp() {
        val properties = UploadProperties()
        properties.location = "test-uploads"
        uploadService = UploadService(scrImageService, properties)
        uploadService.init()
    }

    @Test
    @DisplayName("upload file")
    fun upload() {
        val mockFile = MockMultipartFile("file", "test.jpg", "image/jpg", "test file".toByteArray())
        uploadService.upload(mockFile)
        val file = uploadService.load("test.jpg")
        assertThat(file).isNotNull()
    }

    @Test
    @DisplayName("fail to upload empty file")
    fun uploadEmptyFile() {
        val mockFile = MockMultipartFile("file", "test.jpg", "image/jpg", ByteArray(0))
        val exception = assertThrows<UploadException> {
            uploadService.upload(mockFile)
        }
        assertThat(exception.message).isEqualTo("Failed to store empty file test.jpg")
    }
}