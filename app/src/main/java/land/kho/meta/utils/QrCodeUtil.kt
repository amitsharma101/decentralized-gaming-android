package land.kho.meta.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import land.kho.meta.BuildConfig
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

const val qrDirectoryName = "cacheQr"

object QrCodeUtil {

    fun shareBitmap(bitmap: Bitmap, context: Context, fileName: String): Uri {
        val file = File(getTempFolder(context = context), fileName)
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".fileProvider",
            file
        )

    }

    private fun getTempFolder(context: Context): String? {
        val tempDirectory = File(
            context.getExternalFilesDir(null).toString() + File.separator + qrDirectoryName
        )
        if (!tempDirectory.exists()) {
            println("creating directory: temp")
            tempDirectory.mkdir()
        }
        return tempDirectory.absolutePath
    }


    fun cleanTempFolder(context: Context) {
        val tempDirectory = File(
            context.getExternalFilesDir(null).toString() + File.separator + qrDirectoryName
        )
        if (tempDirectory.exists()) {
            try {
                for (f in tempDirectory.listFiles()!!) f.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}