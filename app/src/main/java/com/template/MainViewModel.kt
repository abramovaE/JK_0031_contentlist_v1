package com.template

import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.*
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor
import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions
import kotlinx.coroutines.*
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream

class MainViewModel(private var cacheDir: File, private var fileNamesList: Array<String>, private var assets: AssetManager): ViewModel() {


    private lateinit var cacheFiles: Array<String>
    private var currentIndex = MutableLiveData<Int>()


    fun getCurrentIndex(): LiveData<Int>{
        return currentIndex
    }

    fun decrementCurrentIndex(){
        var value = currentIndex.value?:0
        if(value > 0) value -= 1
        currentIndex.postValue(value)
    }

    fun incrementCurrentIndex(){
        var value = currentIndex.value?:0
        if(value < cacheFiles.size - 1)value += 1
        currentIndex.postValue(value)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    fun load(){
        cacheFiles = Array(fileNamesList.size) { "" }
        viewModelScope.launch(handler){
            withContext(Dispatchers.IO){
                    for ((index, s) in fileNamesList.withIndex()) {
                        launch {
                            loadOne(index)
                        }
                    }
                Log.d("TAG", "loaded all")
            }
        }
    }

    fun getCacheFile(index: Int): String {
        return cacheFiles[index]
    }

    private suspend fun loadOne(index: Int) = withContext(Dispatchers.IO) {
        val fileDir = File(cacheDir.absolutePath + "/file${index}")
        fileDir.mkdirs()
        val htmlFile = File.createTempFile("tempHtml${index}", ".html", fileDir)
        htmlFile.deleteOnExit()
        cacheFiles[index] = htmlFile.absolutePath
        val path = "pages/${fileNamesList[index]}"
        val document = XWPFDocument(assets.open(path))
        saveDocToCache(document, htmlFile)
        if(currentIndex.value == null){
            currentIndex.postValue(0)
        }
    }


    private fun saveDocToCache(document: XWPFDocument, htmlFile: File){
        htmlFile.parentFile.mkdirs()
        val options = XHTMLOptions.create()
        val imgFolder = File(cacheDir.absolutePath + "/${htmlFile.name}_img")
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


    @Suppress("UNCHECKED_CAST")
    class Factory(private var cacheDir: File, private var assets: AssetManager) : ViewModelProvider.NewInstanceFactory() {
        private var fileNamesList = assets.list("pages")!!
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            when (modelClass) {
                MainViewModel::class.java -> MainViewModel(cacheDir, fileNamesList, assets) as T
                else -> throw IllegalArgumentException()
            }
            return MainViewModel(cacheDir, fileNamesList, assets) as T
        }
    }
}