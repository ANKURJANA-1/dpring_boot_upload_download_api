package com.converter.pdf_to_csv.repository


import com.converter.pdf_to_csv.models.DocumentStorageProperties
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DocumentStoragePropertiesRepo : JpaRepository<DocumentStorageProperties, Long> {

    @Query(
        "select * from document_storage_properties where user_id=:userId and document_format=:docType",
        nativeQuery = true
    )
    fun checkDocumentByUserId(userId: Int, docType: String): Optional<DocumentStorageProperties>?


    @Query(
        "SELECT file_name from document_storage_properties where user_id=:userId and document_format=:docType",
        nativeQuery = true
    )
    fun getUploadDocumentPath(userId: Int, docType: String): String

}