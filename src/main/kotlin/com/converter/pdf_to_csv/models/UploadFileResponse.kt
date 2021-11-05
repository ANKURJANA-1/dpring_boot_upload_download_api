package com.converter.pdf_to_csv.models

data class UploadFileResponse(
    var fileName: String = "",
    var fileDownloadUri: String = "",
    var fileType: String = "",
    var size: Long = 0L
)