package com.example.weathersnap.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

object ImageCompressor {
    
    data class CompressionResult(
        val compressedFile: File,
        val originalSizeBytes: Long,
        val compressedSizeBytes: Long
    )

    fun compressImage(context: Context, originalFile: File): CompressionResult? {
        if (!originalFile.exists()) return null
        
        val originalSize = originalFile.length()
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(originalFile.absolutePath, options)
        
        val maxDimension = 1280
        val srcWidth = options.outWidth
        val srcHeight = options.outHeight
        var inSampleSize = 1
        if (srcWidth > maxDimension || srcHeight > maxDimension) {
            val halfHeight: Int = srcHeight / 2
            val halfWidth: Int = srcWidth / 2
            while (halfHeight / inSampleSize >= maxDimension || halfWidth / inSampleSize >= maxDimension) {
                inSampleSize *= 2
            }
        }
        
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath, options) ?: return null
        
        val compressedFile = FileUtils.getTempImageFile(context)
        FileOutputStream(compressedFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out)
        }
        
        return CompressionResult(
            compressedFile = compressedFile,
            originalSizeBytes = originalSize,
            compressedSizeBytes = compressedFile.length()
        )
    }
}
