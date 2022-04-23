package com.template

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.template.databinding.ActivityMenuBinding

import java.io.File
import kotlinx.coroutines.*


class MenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val urlWebView = binding.webView
        urlWebView.webViewClient = WebViewClient()

        urlWebView.webChromeClient = WebChromeClient()

        urlWebView.settings.javaScriptEnabled = true
        urlWebView.settings.useWideViewPort = true
        urlWebView.settings.allowFileAccess = true

        urlWebView.settings.builtInZoomControls = true
        urlWebView.settings.loadWithOverviewMode = true

        urlWebView.scrollBarSize = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        urlWebView.isScrollbarFadingEnabled = true

        val mainViewModel = ViewModelProvider((this),
            MainViewModel.Factory(cacheDir, assets))[MainViewModel::class.java]


        mainViewModel.load()

        mainViewModel.getCurrentIndex().observe(this, {
            setFileToWebView(File(mainViewModel.getCacheFile(it)))
        })


        binding.aboutBtn.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.to, R.anim.from_y0)
        }

        binding.backBtn.setOnClickListener {
            mainViewModel.decrementCurrentIndex()
        }
        binding.forwardBtn.setOnClickListener {
            mainViewModel.incrementCurrentIndex()
        }
    }


    private fun setFileToWebView(htmlFile: File){
        val urlWebView = binding.webView
        htmlFile.setReadable(true)
        urlWebView.loadUrl(htmlFile.absolutePath)
    }

    override fun onBackPressed() {
        val intent = Intent(this@MenuActivity, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.to, R.anim.from2)
    }
}