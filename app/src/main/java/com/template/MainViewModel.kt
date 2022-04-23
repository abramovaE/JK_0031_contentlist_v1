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
import java.util.*

class MainViewModel(var cacheDir: File, var fileNamesList: Array<String>, var assets: AssetManager): ViewModel() {


    private lateinit var cacheFiles: Array<String>
    private var currentIndex = MutableLiveData<Int>()


    fun getCurrentIndex(): LiveData<Int>{
        return currentIndex
    }
    fun setCurrentIndex(index: Int){
        currentIndex.postValue(index)
    }
    fun decrCurrentIndex(){
        Log.d("TAG", "decr index: " + currentIndex.value)
        var value = currentIndex.value?:0
        if(value > 0){
            value -= 1
        }
        Log.d("TAG", "decr index post value: " + value)
        currentIndex.postValue(value)
    }

    fun incrCurrentIndex(){
        var value = currentIndex.value?:0
        if(value < cacheFiles.size - 1){
            value += 1
        }
        currentIndex.postValue(value)
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    fun load(){
        cacheFiles = Array(fileNamesList.size, { it -> "" })

        viewModelScope.launch(handler){
            withContext(Dispatchers.IO){
//                var job = async {
                    for ((index, s) in fileNamesList.withIndex()) {
                        launch {
                            loadOne(index)
                        }
                    }
//                }
//                job.join()
//                currentIndex.postValue(0)
                Log.d("TAG", "loaded all")
            }
        }
    }

    fun getCacheFile(index: Int): String {
        return cacheFiles[index]
    }

    suspend fun loadOne(index: Int) = withContext(Dispatchers.IO) {
        var fileDir = File(cacheDir.absolutePath + "/file${index}")
        fileDir.mkdirs()
        var htmlFile = File.createTempFile("tempHtml${index}", ".html", fileDir)
        htmlFile.deleteOnExit()
        Log.d("TAG", "cachefilesval: ${Arrays.toString(cacheFiles)}")
        cacheFiles[index] = htmlFile.absolutePath
        var path = "pages/${fileNamesList[index]}"
        var document = XWPFDocument(assets.open(path))
        saveDocToCache(document, htmlFile)
        Log.d("TAG", "load: ${htmlFile.absolutePath}")

        if(currentIndex.value == null){
            currentIndex.postValue(0)
        }
    }


    fun saveDocToCache(document: XWPFDocument, htmlFile: File){
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


    @Suppress("UNCHECKED_CAST")
    class Factory(var cacheDir: File, var assets: AssetManager) : ViewModelProvider.NewInstanceFactory() {

        var fileNamesList = assets.list("pages")!!


        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            when (modelClass) {
                MainViewModel::class.java -> MainViewModel(cacheDir, fileNamesList, assets) as T
                else -> throw IllegalArgumentException()
            }
            return MainViewModel(cacheDir, fileNamesList, assets) as T
        }
    }
}