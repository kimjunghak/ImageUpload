package com.fileuploader.controller

import com.fileuploader.service.UploadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileController(
    private val uploadService: UploadService,
) {

    @GetMapping("/image")
    fun image(): ResponseEntity<List<String>> {
        val loadAll = uploadService.loadAll()
        return ResponseEntity.ok(loadAll.toList())
    }

    @PostMapping("/upload")
    fun upload(@RequestParam("file") multipartFile: MultipartFile): ResponseEntity<Unit> {
        uploadService.upload(multipartFile)
        return ResponseEntity.ok().build()
    }
}