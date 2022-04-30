package com.template

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.webkit.*

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.template.databinding.ActivityMenuBinding

import java.io.File
import kotlinx.coroutines.*


class MenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val urlWebView = binding.webView
        urlWebView.webViewClient = object: WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?): Boolean {
                return false
            }
        }

        urlWebView.webChromeClient = WebChromeClient()
        urlWebView.settings.useWideViewPort = true
        urlWebView.settings.allowFileAccess = true
        urlWebView.settings.builtInZoomControls = true
        urlWebView.settings.loadWithOverviewMode = true
        urlWebView.scrollBarSize = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        urlWebView.isScrollbarFadingEnabled = true
        urlWebView.settings.displayZoomControls = false
        urlWebView.settings.javaScriptEnabled = true

        urlWebView.settings.setSupportZoom(true)
        urlWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        urlWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)


        val mainViewModel = ViewModelProvider((this),
            MainViewModel.Factory(cacheDir, assets))[MainViewModel::class.java]
        mainViewModel.load()

        mainViewModel.getCurrentIndex().observe(this, {
            setFileToWebView(it, File(mainViewModel.getCacheFile(it)))
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

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    private fun setFileToWebView(newIndex: Int, htmlFile: File){
        val urlWebView = binding.webView
        binding.webView.visibility = View.GONE
        htmlFile.setReadable(true)
        urlWebView.loadUrl(htmlFile.absolutePath)

        val slideEnd = Slide(Gravity.END)
        val slideStart = Slide(Gravity.START)

        val anim: Slide
        if (currentIndex < newIndex) {
            anim = slideEnd
            anim.duration = 500
            TransitionManager.beginDelayedTransition(binding.root, anim)
        }
        else if (currentIndex > newIndex) {
            anim = slideStart
            anim.duration = 500
            TransitionManager.beginDelayedTransition(binding.root, anim)
        }

        binding.webView.visibility = View.VISIBLE
        currentIndex = newIndex
    }

    override fun onBackPressed() {
        val intent = Intent(this@MenuActivity, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.to, R.anim.from2)
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
    }
}