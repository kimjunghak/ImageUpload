package com.fileuploader.controller

import com.fileuploader.service.UploadService
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.stream.Stream

@WebMvcTest
@ExtendWith(MockitoExtension::class)
class FileControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var uploadService: UploadService

    @Test
    @DisplayName("get image")
    fun image() {
        given(uploadService.loadAll()).willReturn(Stream.of("test.jpg", "test2.jpg"))

        mvc.perform(get("/image"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<String>(2)))
            .andExpect(jsonPath("$[0]").value("test.jpg"))
            .andExpect(jsonPath("$[1]").value("test2.jpg"))
    }

    @Test
    @DisplayName("upload file")
    fun upload() {
        val multipartFile = MockMultipartFile("file", "test.jpg", "image/jpg", "test file".toByteArray())

        mvc.perform(
            multipart("/upload")
                .file("file", multipartFile.bytes)
        )
            .andExpect(status().isOk)

    }
}