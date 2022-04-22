package com.template

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity
import com.template.databinding.ActivityMenuBinding
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter
import org.apache.poi.xwpf.usermodel.XWPFDocument

import java.io.File
import java.io.FileOutputStream

class MenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var filesList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filesList = assets.list("pages")!!
        Log.d("TAG", "pages: ${filesList.contentToString()}")

        var currentIndex = 0
        setIndexContent(currentIndex)

        binding.aboutBtn.setOnClickListener { v->
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener { v->
            if(currentIndex > 0) {
                currentIndex -=1
                setIndexContent(currentIndex)
            }
        }
        binding.forwardBtn.setOnClickListener { v->
            currentIndex += 1
            setIndexContent(currentIndex)
        }
    }


    private fun setIndexContent(currentIndex: Int){
        val htmlFile = File.createTempFile("tempHtml${currentIndex}", ".html", cacheDir)
        convertDocxToHtml(currentIndex, htmlFile)
        setFileToWebView(htmlFile)
    }

    private fun convertDocxToHtml(currentIndex: Int, htmlFile: File){
        if(currentIndex < filesList.size){
            val path = "pages/${filesList[currentIndex]}"
            Log.d("TAG", "path: $path")
            val document = XWPFDocument(assets.open("pages/${filesList[currentIndex]}"))
            saveDocToCache(document, htmlFile)
        }
    }

    private fun saveDocToCache(document: XWPFDocument, htmlFile: File){
        htmlFile.parentFile.mkdirs()
        val out = FileOutputStream(htmlFile)
        XHTMLConverter.getInstance().convert(document, out, null)
        out.close()
        document.close()
    }
    private fun setFileToWebView(htmlFile: File){
        val urlWebView = binding.webView
        urlWebView.webViewClient = WebViewClient()
        urlWebView.settings.javaScriptEnabled = true
        urlWebView.settings.useWideViewPort = true
        urlWebView.settings.allowFileAccess = true
        urlWebView.settings.builtInZoomControls = true
        htmlFile.setReadable(true)
        urlWebView.loadUrl(htmlFile.absolutePath)
    }
}