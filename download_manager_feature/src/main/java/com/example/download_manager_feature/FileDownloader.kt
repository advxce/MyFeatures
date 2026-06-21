package com.example.download_manager_feature

interface FileDownloader {
    fun getDownloadProgress(fileId: String): kotlinx.coroutines.flow.Flow<DownloadItem>
    fun startDownload(fileId: String)
    fun pauseDownload(fileId: String)
}