package com.template

import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor
import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream


object Data{

    fun saveDocToCache(document: XWPFDocument, htmlFile: File, cacheDir: File){
        htmlFile.parentFile.mkdirs()
        val options = XHTMLOptions.create()
        var imgFolder = File(cacheDir.absolutePath + "/${htmlFile.name}_img")
        imgFolder.mkdirs()
        options.extractor = FileImageExtractor(htmlFile.parentFile)
        val out = FileOutputStream(htmlFile)
        try {
            XHTMLConverter.getInstance().convert(document, out, options)
        } catch (ex: Exception) {
            when(ex) {
                is XWPFConverterException -> {
                }
                else -> throw ex
            }
        }
        out.close()
        document.close()
    }
}