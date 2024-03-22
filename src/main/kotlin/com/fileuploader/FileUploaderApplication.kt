package com.fileuploader

import com.fileuploader.service.UploadService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class FileUploaderApplication {
    @Bean
    fun run(uploadService: UploadService) = CommandLineRunner {
        uploadService.deleteAll()
        uploadService.init()
    }
}

fun main(args: Array<String>) {
    runApplication<FileUploaderApplication>(*args)
}
