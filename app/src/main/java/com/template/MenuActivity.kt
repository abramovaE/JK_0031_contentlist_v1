package com.template

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.template.databinding.ActivityMenuBinding
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter
import org.apache.poi.xwpf.usermodel.XWPFDocument

import java.io.File
import java.io.FileOutputStream
import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException

import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions
import kotlinx.coroutines.*


class MenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var fileNamesList = assets.list("pages")!!
//        var cacheFiles = Array(fileNamesList.size, {it->""})

        var mainViewModel = ViewModelProvider((this),
            MainViewModel.Factory(cacheDir, assets)).get(MainViewModel::class.java)


        mainViewModel.load()
//        mainViewModel.setCurrentIndex(0)

        mainViewModel.getCurrentIndex().observe(this, {
            Log.d("TAG", "new index: " + it)
            setFileToWebView(File(mainViewModel.getCacheFile(it)))
        })


//        mainViewModel.getCacheFiles().observe(this, {
//            if(it != null) {
//                cacheFiles = it
//                setFileToWebView(File(it[0]))
//            }
//        })


        binding.aboutBtn.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.to, R.anim.from_y0)
        }

        binding.backBtn.setOnClickListener {
            mainViewModel.decrCurrentIndex()
        }
        binding.forwardBtn.setOnClickListener {
            mainViewModel.incrCurrentIndex()
        }
    }


    private fun setFileToWebView(htmlFile: File){
        val urlWebView = binding.webView
        urlWebView.webViewClient = WebViewClient()
        urlWebView.webChromeClient = WebChromeClient()
        urlWebView.webViewClient = WebViewClient()

        urlWebView.settings.javaScriptEnabled = true
        urlWebView.settings.useWideViewPort = true
        urlWebView.settings.allowFileAccess = true

        urlWebView.settings.builtInZoomControls = true
        urlWebView.settings.loadWithOverviewMode = true

        urlWebView.scrollBarSize = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        urlWebView.isScrollbarFadingEnabled = true
        htmlFile.setReadable(true)
        urlWebView.loadUrl(htmlFile.absolutePath)
    }

    override fun onBackPressed() {
        val intent = Intent(this@MenuActivity, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.to, R.anim.from2)
    }
}