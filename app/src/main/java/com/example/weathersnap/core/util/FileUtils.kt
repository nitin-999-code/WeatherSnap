package com.example.weathersnap.core.util

import android.content.Context
import java.io.File

object FileUtils {
    fun getTempImageFile(context: Context): File {
        val dir = context.cacheDir
        return File.createTempFile("temp_img_", ".jpg", dir)
    }

    fun getFinalImageFile(context: Context, filename: String): File {
        val dir = File(context.filesDir, "reports")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir, filename)
    }
}
