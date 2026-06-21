package com.example.download_manager_feature

data class DownloadItem(
    val id: String,
    val fileName: String,
    val progress: Int,
    val status: DownloadStatus,
    val url: String
)


enum class DownloadStatus { IDLE, DOWNLOADING, PAUSED, COMPLETED }