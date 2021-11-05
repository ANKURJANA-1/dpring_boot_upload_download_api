package com.converter.pdf_to_csv.services

import com.converter.pdf_to_csv.exceptions.DocumentStorageException
import com.converter.pdf_to_csv.models.DocumentStorageProperties
import com.converter.pdf_to_csv.repository.DocumentStoragePropertiesRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


@Service
class DocumentService(
) {

    private val fileStorageProperties: DocumentStorageProperties = DocumentStorageProperties()

    @Autowired
    private lateinit var documentStoragePropertiesRepo: DocumentStoragePropertiesRepo

    private var fileStorageLocation: Path? = null


    init {
        fileStorageLocation = Paths.get(fileStorageProperties.uploadDir)
            .toAbsolutePath()
            .normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw DocumentStorageException(
                "Could not create the directory where the uploaded files will be stored.",
                e.fillInStackTrace()
            )
        }
    }


    fun storeFile(file: MultipartFile, userId: Int, docType: String): String {

        val originalFileName: String = StringUtils.cleanPath(file.originalFilename!!)
        var fileName: String = ""

        try {
            if (originalFileName.contains("..")) {
                throw DocumentStorageException("Sorry! Filename contains invalid path sequence $originalFileName")
            }

            var fileExtension: String = ""

            fileExtension = try {
                originalFileName.substring(originalFileName.lastIndexOf("."))
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            try {
                fileName = "$userId${"_"}$fileExtension"

                val targetLocation: Path = fileStorageLocation!!.resolve(fileName)
                Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
                val doc: Optional<DocumentStorageProperties> =
                    documentStoragePropertiesRepo.checkDocumentByUserId(userId, docType)!!

                if (!doc.isEmpty) {
                    doc.get().documentType = file.contentType!!
                    doc.get().fileName = fileName
                    documentStoragePropertiesRepo.save(doc.get())
                } else {
                    val newDoc = DocumentStorageProperties()
                    newDoc.apply {
                        this.userId = userId
                        this.documentFormat = file.contentType!!
                        this.fileName = fileName
                        this.documentType = docType

                        documentStoragePropertiesRepo.save(newDoc)
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }


            return fileName
        } catch (e: Exception) {
            throw DocumentStorageException("Could not store file $fileName. Please try again!", e)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {

        try {
            val filePath: Path =
                fileStorageLocation!!.resolve(fileName)
                    .normalize()
            val resource = UrlResource(filePath.toUri())

            if (resource.exists()) {
                return resource
            } else {
                throw FileNotFoundException("File not found $fileName")
            }
        } catch (e: Exception) {
            throw FileNotFoundException("File not found $fileName")
        }

    }

    fun getDocumentName(userId: Int, docType: String): String {
        return documentStoragePropertiesRepo.getUploadDocumentPath(userId, docType)
    }

}