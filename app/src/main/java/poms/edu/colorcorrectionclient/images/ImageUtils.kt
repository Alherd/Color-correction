package poms.edu.colorcorrectionclient.images

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt


fun getScaledBitmapForContainer(
    originalBitmap: Bitmap,
    container: View
): Bitmap {

    fun min(a: Float, b: Float) = if (a < b) a else b

    val ratio = min(
        container.height.toFloat() / originalBitmap.height.toFloat(),
        container.width.toFloat() / originalBitmap.width.toFloat()
    )

    val scaledBitmap = Bitmap.createScaledBitmap(
        originalBitmap,
        (originalBitmap.width * ratio).roundToInt(),
        (originalBitmap.height * ratio).roundToInt(),
        true
    )
    return scaledBitmap
}

fun drawableToFile(drawable: Drawable, filesDir: File): File {
    val bitmap = if (drawable is BitmapDrawable) {
        drawable.bitmap
    }
        else throw IllegalArgumentException()

    val tmpFile = File(filesDir, "tmp")
    val outputStream = BufferedOutputStream(FileOutputStream(tmpFile))
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return tmpFile
}