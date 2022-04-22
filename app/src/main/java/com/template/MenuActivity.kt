package com.template

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityMenuBinding
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.BufferedReader

import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets


class MenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        var input = assets.open("pril2_fns891_09122020.docx")
//        var file = File.createTempFile("temp", ".docx", cacheDir )
//        Log.d("TAG", "tempfile: " + file.absolutePath)
//        var out = file.outputStream()
//        input.use { it ->
//            out.use{ it ->
//                input.copyTo(out)
//            }
//        }
////        input.close()
//        out.close()

//        val zipFile = ZipFile(file)
//        val zipEntry = zipFile.getEntry("word/document.xml")
//        var zipInput = zipFile.getInputStream(zipEntry)
//
//        var stringBuilder = StringBuilder()
//        val reader = BufferedReader(InputStreamReader(zipInput, StandardCharsets.UTF_8))
//
//        try {
//            var line = reader.readLine()
//            while (line != null) {
//                stringBuilder.append(line)
//                line = reader.readLine()
//            }
//        } finally {
//            reader.close()
//        }


//        poi
        val htmlFile = File.createTempFile("caaa", ".html", cacheDir)
        Log.d("TAG", "temp: " + htmlFile.absolutePath)

        val document = XWPFDocument(assets.open("pril2_fns891_09122020.docx"))
        htmlFile.parentFile.mkdirs()
        val out = FileOutputStream(htmlFile)
        XHTMLConverter.getInstance().convert(document, out, null)


        var input = htmlFile.inputStream()

        var stringBuilder = StringBuilder()
        val reader = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8))


            var line = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }

        input.close()
        reader.close()
//        Log.d("TAG", "temp: " + stringBuilder.toString())



        val urlWebView = binding.webView
        urlWebView.setWebViewClient(WebViewClient())
        urlWebView.getSettings().setJavaScriptEnabled(true)
        urlWebView.getSettings().setUseWideViewPort(true)
        urlWebView.getSettings().setAllowFileAccess(true)
        val summary = "<html><body><p><strong>12345</strong></p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>&nbsp;</p>\n" +
                "<p>67890Qwertyйцукен</p></body></html>"
//        urlWebView.loadData(stringBuilder.toString(), "text/html", null)
        htmlFile.setReadable(true)
        urlWebView.loadUrl(htmlFile.absolutePath)


//        urlWebView.lo
        binding.aboutBtn.setOnClickListener { v->
            var intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }
//
//        binding.backBtn.setOnClickListener { v-> binding.webView.findNext(true)}
//        binding.forwardBtn.setOnClickListener { v-> binding.webView.findNext(true) }


    }

}