package com.example.download_manager_feature

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DownloadViewModelFactory(
    private val context: Context,
    private val fileDownloadApi: FileDownloadApi
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DownloadViewModel::class.java)){
            return DownloadViewModel(DownloadRepoRetrofit(ServiceLocator.fileDownloadApi)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}