package com.example.unsplashdaggermvvm.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.unsplashdaggermvvm.repository.UnsplashRepository
import com.example.unsplashdaggermvvm.utils.ERROR_DOWNLOADING
import com.example.unsplashdaggermvvm.utils.HAS_DOWNLOADED
import com.example.unsplashdaggermvvm.utils.HAS_SAVED
import com.example.unsplashdaggermvvm.utils.STARTING_DOWNLOAD
import com.example.unsplashdaggermvvm.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class SingleImageViewModel @Inject constructor(
    private val app: Application,
    private val unsplashRepository: UnsplashRepository
): AndroidViewModel(app) {

    var imageString: String? = null
    private val _notifyDownloading = MutableLiveData<Int>()
    val notifyDownLoading: LiveData<Int> get() = _notifyDownloading

    suspend fun getBitmapFromUrl(src: String,photoId: String) {
        withContext(Dispatchers.Main) {
            _notifyDownloading.value = STARTING_DOWNLOAD
        }
        try {
            val url = URL(src)
            val connection: HttpURLConnection =  url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            saveImage(bitmap,photoId).also {
                withContext(Dispatchers.Main) {
                    _notifyDownloading.value = HAS_DOWNLOADED
                }
            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                _notifyDownloading.value = ERROR_DOWNLOADING
                app.applicationContext.toast("There was a problem downloading the image")
            }
        }
    }

    fun saveImage(bitmap: Bitmap, photoId: String)  = viewModelScope.launch(Dispatchers.Default) {
        try {
            val fos: OutputStream?
            var imageFile: File? = null
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = app.applicationContext.contentResolver
                val contentValues = ContentValues()
                contentValues.put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "Image${System.currentTimeMillis()}"
                )
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + File.separator.toString() + "Unsplash Images"
                )
                val imageUri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                )
                fos = imageUri?.let { resolver.openOutputStream(it)  }
            } else {
                val myDir = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                    ).toString() + File.separator.toString() + "UnsplashImages"
                )
                myDir.mkdir()
                imageFile = File(myDir, "Image${System.currentTimeMillis()}.png")
                if(imageFile.exists()) imageFile.delete()
                imageFile.createNewFile()
                if(!imageFile.exists()) {
                    imageFile.mkdirs()
                }

                MediaScannerConnection.scanFile(
                    app.applicationContext,
                    arrayOf(imageFile.toString()),
                    null,
                    null
                )
                fos = FileOutputStream(imageFile)
            }
            fos?.let { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) }
            fos?.flush()
            fos?.close()

            withContext(Dispatchers.Main) {
                _notifyDownloading.value = HAS_SAVED
                app.applicationContext.toast("Image saved successfully")
                try {
                    unsplashRepository.sendDownload(photoId)
                } catch (_: Exception) {

                }
            }
        } catch (e: Exception) {
            Log.d("SavingImage", e.toString())
            withContext(Dispatchers.Main) {
                _notifyDownloading.value = ERROR_DOWNLOADING
                app.applicationContext.toast("There was a problem savings this image")
            }
        }
    }
}