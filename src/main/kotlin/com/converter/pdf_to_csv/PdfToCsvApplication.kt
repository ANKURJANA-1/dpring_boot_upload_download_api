package com.converter.pdf_to_csv

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdfToCsvApplication

fun main(args: Array<String>) {
	runApplication<PdfToCsvApplication>(*args)
}
