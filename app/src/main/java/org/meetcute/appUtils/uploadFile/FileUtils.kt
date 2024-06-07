package org.meetcute.appUtils.uploadFile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtils {

    fun getFileFromContentUri(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp_file") // Temp file to store the content
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                val outputStream = FileOutputStream(file)
                outputStream.use { output ->
                    val buffer = ByteArray(4 * 1024) // 4 KB buffer size
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}

