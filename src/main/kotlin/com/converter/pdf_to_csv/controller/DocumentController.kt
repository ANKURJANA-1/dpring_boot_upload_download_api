package com.converter.pdf_to_csv.controller

import com.converter.pdf_to_csv.models.UploadFileResponse
import com.converter.pdf_to_csv.services.DocumentService
import com.converter.pdf_to_csv.utils.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("")
class DocumentController {

    @Autowired
    private lateinit var documentStorageService: DocumentService

    @PostMapping("/uploadFile")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("userId") userId: String,
        @RequestParam("docType") docType: String
    ): UploadFileResponse {
        val fileName = documentStorageService.storeFile(file, userId.toInt(), docType)
        val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName).toUriString()

        return UploadFileResponse(
            fileName,
            fileDownloadUri,
            file.contentType!!,
            file.size
        )
    }

    @GetMapping("/download")
    fun downloadFile(
        @RequestParam("userId") userId: Int,
        @RequestParam("docType") docType: String,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {
        val fileName = documentStorageService.getDocumentName(userId, docType)
        var resource: Resource? = null

        if (fileName.isNotBlank() && fileName.isNotEmpty()) {
            try {
                resource = documentStorageService.loadFileAsResource(fileName)
            } catch (e: Exception) {
                println(e)
            }

            var contentType: String? = null
            try {
                contentType = request.servletContext.getMimeType(resource!!.file.absolutePath)
            } catch (e: Exception) {
                println(e)
            }
            if (contentType == null) {
                contentType = "application/octet-stream"
            }
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource!!.filename + "\"")
                .body(resource)


        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/convert")
    fun convertPdfToCsv(
        @RequestParam("fileName") fileName: String
    ) {
        Utils.pdfToCsv(fileName)
    }


}