package com.converter.pdf_to_csv.utils

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import org.springframework.context.annotation.Bean
import java.io.FileWriter

object Utils {

    @Bean
    fun pdfToCsv(fileName: String) {
        val pdfReader: PdfReader = PdfReader(
            "${fileName}${".pdf"}"
        )

        val page: Int = pdfReader.numberOfPages
        val csvWriter: FileWriter = FileWriter(
            "${fileName}${".csv"}"
        )

        for (i in 1..page) {
            val content: String = PdfTextExtractor.getTextFromPage(pdfReader, i)
            val splitContent: List<String> = content.split("\n")

            for (j in splitContent) {
                csvWriter.append(j.replace(" ", ","))
                csvWriter.append("\n")
            }
        }
        csvWriter.flush()
        csvWriter.close()
    }
}