package com.converter.pdf_to_csv.models

import org.springframework.context.annotation.Bean
import java.io.Serializable
import javax.persistence.*

@Entity
@Table
data class DocumentStorageProperties(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    var id: Long = 1L,

    @Column(name = "user_id")
    var userId: Int = 0,

    @Column(name = "file_name")
    var fileName: String = "",

    @Column(name = "document_type")
    var documentType: String = "",

    @Column(name = "document_format")
    var documentFormat: String = "",

    @Column(name = "upload_dir")
    var uploadDir: String = ""
) : Serializable
