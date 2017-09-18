package com.zalologin.ebook

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.zalologin.R
import java.io.*
import java.util.ArrayList
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * BookUtils
 *
 * Created by HOME on 9/14/2017.
 */
class BookUtils {
    companion object {
        fun bindAllBook(context: Context): List<String>? {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return null
            }

            val localBooks = ArrayList<String>()
            val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + "EPUB"
            val file = File(path)
            val files = file.listFiles()
            var i = 0
            for (f: File in files) {
                System.out.println("file" + (i++) + f.absoluteFile);
            }
            return localBooks
        }

        // Unzip epub file
        @Throws(IOException::class)
        fun unzip(inputZip: String, destinationDirectory: String) {
            val BUFFER = 2048
            val zipFiles = ArrayList<String>()
            val sourceZipFile = File(inputZip)
            val unzipDestinationDirectory = File(destinationDirectory)
            unzipDestinationDirectory.mkdir()

            val zipFile: ZipFile
            zipFile = ZipFile(sourceZipFile, ZipFile.OPEN_READ)
            val zipFileEntries = zipFile.entries()

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {

                val entry = zipFileEntries.nextElement() as ZipEntry
                val currentEntry = entry.name
                val destFile = File(unzipDestinationDirectory, currentEntry)

                if (currentEntry.endsWith(".zip")) {
                    zipFiles.add(destFile.absolutePath)
                }

                val destinationParent = destFile.parentFile
                destinationParent.mkdirs()

                if (!entry.isDirectory) {
                    val `is` = BufferedInputStream(
                            zipFile.getInputStream(entry))
                    var currentByte: Int
                    // buffer for writing file
                    val data = ByteArray(BUFFER)

                    val fos = FileOutputStream(destFile)
                    val dest = BufferedOutputStream(fos, BUFFER)

                    currentByte = `is`.read(data, 0, BUFFER)
                    while (currentByte != -1) {
                        dest.write(data, 0, currentByte)
                    }
                    dest.flush()
                    dest.close()
                    `is`.close()
                }
            }
            zipFile.close()
        }
    }
}