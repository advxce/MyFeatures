package com.example.download_manager_feature

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.use
import java.io.File
import java.io.FileOutputStream

class DownloadRepoRetrofit(
    private val downloadApi: FileDownloadApi
) {

    private val downloadsDirectory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    suspend fun downloadFile(fileUrl: String, fileName: String): Flow<Pair<DownloadStatus, Int>> =
        flow {

            try {
                emit(DownloadStatus.DOWNLOADING to 0)
                val response = downloadApi.downloadFile(fileUrl)
                if (response.isSuccessful) {
                    val request = response.body()
                    val fixSizeArray = ByteArray(4096)
                    val fileSize = request?.contentLength() ?: 0L
                    val outputFile = File(downloadsDirectory, fileName)
                    request?.byteStream().use { inputStream ->
                        var fileSizeDownloaded: Long = 0
                        val outputStream = FileOutputStream(outputFile)
                        outputStream.use { outputStream ->
                            var readBytes: Int = 0
                            inputStream?.let {
                                while (inputStream.read(fixSizeArray)
                                        .also { readBytes = it } != -1
                                ) {
                                    outputStream.write(fixSizeArray, 0, readBytes)
                                    fileSizeDownloaded += readBytes
                                    println("file download: $fileSizeDownloaded of $fileSize")
                                    if(fileSize>0){
                                        val progress = ((fileSizeDownloaded * 100) / fileSize).toInt()
                                        emit(DownloadStatus.DOWNLOADING to progress)
                                    }
                                }
                            }

                        }
                    }
                    emit(DownloadStatus.COMPLETED to 100)
                } else {
                    emit(DownloadStatus.IDLE to 0)
                }
            } catch (e: Exception) {
                emit(DownloadStatus.IDLE to 0)
            }


        }.flowOn(Dispatchers.IO)

}