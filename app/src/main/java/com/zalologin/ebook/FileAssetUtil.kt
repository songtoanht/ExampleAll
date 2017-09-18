package com.zalologin.ebook

import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import android.util.Log
import java.io.*


/**
 * //Todo
 *
 * Created by HOME on 9/15/2017.
 */
class FileAssetUtil {

    companion object {
        fun copyAssets(context: Context) {
            val assetManager = context.getAssets()
            var files: Array<String>? = null
            try {
                files = assetManager.list("")
            } catch (e: IOException) {
                Log.e("tag", "Failed to get asset file list.", e)
            }

            if (files != null)
                for (filename in files) {
                    var `in`: InputStream? = null
                    var out: OutputStream? = null
                    try {
                        `in` = assetManager.open(filename)
                        val outFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).absolutePath  + File.separator + "EPUB" + File.separator , filename)
                        out = FileOutputStream(outFile)
                        copyFile(`in`, out)
                    } catch (e: IOException) {
                        Log.e("tag", "Failed to copy asset file: " + filename, e)
                    } finally {
                        if (`in` != null) {
                            try {
                                `in`.close()
                            } catch (e: IOException) {
                                // NOOP
                            }

                        }
                        if (out != null) {
                            try {
                                out.close()
                            } catch (e: IOException) {
                                // NOOP
                            }

                        }
                    }
                }
        }

        @Throws(IOException::class)
        private fun copyFile(`in`: InputStream?, out: OutputStream) {
            val buffer = ByteArray(1024)
            val read = `in`!!.read(buffer)
            while (read != -1) {
                out.write(buffer, 0, read)
            }
        }
    }
}