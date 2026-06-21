package com.example.download_manager_feature

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

class DownloadRepo(
    private val context: Context
) {
    private val downloadManager: DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val _downloadCompleteEvent = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val downloadCompleteEvent = _downloadCompleteEvent.asSharedFlow()

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != -1L) {
                println("Download Completed")
                _downloadCompleteEvent.tryEmit(id)
            }
        }
    }


    init {
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.applicationContext.registerReceiver(
                onDownloadComplete,
                filter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            context.applicationContext.registerReceiver(onDownloadComplete, filter)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun startDownload(fileUrl: String, fileName: String): Long  = withContext(Dispatchers.IO) {
        val downloadRequest: DownloadManager.Request = DownloadManager.Request(
            fileUrl.toUri()
        ).apply {
            setTitle(fileName)
            setDescription("File Download")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        }

        val downloadId = downloadManager.enqueue(downloadRequest)
        return@withContext downloadId
    }

    fun getDownloadProgress(context: Context, downloadId: Long): Int {

        val query = DownloadManager.Query().setFilterById(downloadId)

        val cursor: Cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val bytesDownloadedIdx =
                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            val bytesTotalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
            val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

            if (bytesDownloadedIdx != -1 && bytesTotalIdx != -1) {
                val bytesDownloaded = cursor.getLong(bytesDownloadedIdx)
                val bytesTotal = cursor.getLong(bytesTotalIdx)
                val status = cursor.getInt(statusIdx)

                if (status == DownloadManager.STATUS_SUCCESSFUL) return 100

                if (bytesTotal > 0) {
                    return ((bytesDownloaded * 100) / bytesTotal).toInt()
                }
            }
        }

        cursor.close()
        return 0
    }

    fun clear(){
        context.unregisterReceiver(onDownloadComplete)
    }

}