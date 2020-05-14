package com.wsg.common.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.wsg.common.DefaultLogConfig
import com.wsg.common.utils.FileZipUtils.unZipTo
import com.wsg.common.utils.FileZipUtils.zip
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

fun File.zipInputStream() = ZipInputStream(this.inputStream())

fun File.zipOutputStream() = ZipOutputStream(this.outputStream())

fun InputStream.zipInputStream() = ZipInputStream(this)

fun OutputStream.zipOutputStream() = ZipOutputStream(this)
fun File.smartCreateNewFile(): Boolean {
    if (exists()) return true
    if (parentFile!!.exists()) return createNewFile()
    if (parentFile!!.mkdirs()) return createNewFile()
    return false
}


/**
 * 压缩
 */
fun ZipOutputStream.zipFrom(vararg src: String) {
    val files = src.map { File(it) }
    files.forEach {
        if (it.isFile) {
            zip(arrayOf(it), null)
        } else if (it.isDirectory) {
            zip(it.listFiles()!!, it.name)
        }
    }
    this.close()
}

/**
 * 解压
 */
@TargetApi(Build.VERSION_CODES.N)
infix fun File.unZipTo(path: String) {
    //使用GBK编码,避免压缩中文文件名乱码
    FileZipUtils.checkUnzipFolder(path)
    if (this.length() <= 0) {
        throw RuntimeException("文件太小无法解压！")
    } else {
        ZipFile(this, Charset.forName("GBK")) unZipTo path
    }
}


object FileZipUtils {

    /**
     * 检测路径是否正确
     */
    internal fun checkUnzipFolder(path: String) {
        val file = File(path)
        if (file.isFile) throw RuntimeException("路径不能是文件！")
        if (!file.exists()) {
            if (!file.mkdirs())
                throw java.lang.RuntimeException("文件夹创建失败！")
        }

    }


    internal infix fun ZipFile.unZipTo(path: String) {
        checkUnzipFolder(path)
        for (entry in entries()) {
            //判断是否为文件夹
            if (entry.isDirectory) {
                File("${path}/${entry.name}").mkdirs()
            } else {
                val input = getInputStream(entry)
                val outputFile = File("${path}/${entry.name}")
                if (!outputFile.exists()) outputFile.smartCreateNewFile()
                val output = outputFile.outputStream()
                input.writeTo(output, DEFAULT_BUFFER_SIZE)
            }
        }
    }

      fun generateDefaultLogDirectory(path: String): String? {
        if (Environment.isExternalStorageEmulated()) {
            val sdcardDirectory = Environment.getExternalStorageDirectory()
            val file = File(sdcardDirectory, path)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }
        return null
    }


    internal fun ZipOutputStream.zip(files: Array<File>, path: String?) {
        //前缀,用于构造路径
        val prefix = if (path == null) "" else "$path/"

        if (files.isEmpty()) createEmptyFolder(prefix)

        files.forEach {
            if (it.isFile) {
                val entry = ZipEntry("$prefix${it.name}")
                val ins = it.inputStream().buffered()
                putNextEntry(entry)
                ins.writeTo(this, DEFAULT_BUFFER_SIZE, closeOutput = false)
                closeEntry()
            } else {
                zip(it.listFiles()!!, "$prefix${it.name}")
            }
        }
    }

    /**
     * inputstream内容写入outputstream
     */
    private fun InputStream.writeTo(
        outputStream: OutputStream, bufferSize: Int = 1024 * 2,
        closeInput: Boolean = true, closeOutput: Boolean = true
    ) {

        val buffer = ByteArray(bufferSize)
        val br = this.buffered()
        val bw = outputStream.buffered()
        var length = 0

        while ({ length = br.read(buffer);length != -1 }()) {
            bw.write(buffer, 0, length)
        }

        bw.flush()

        if (closeInput) {
            close()
        }

        if (closeOutput) {
            outputStream.close()
        }
    }

    /**
     * 生成一个压缩文件的文件夹
     */
    private fun ZipOutputStream.createEmptyFolder(location: String) {
        putNextEntry(ZipEntry(location))
        closeEntry()
    }


}