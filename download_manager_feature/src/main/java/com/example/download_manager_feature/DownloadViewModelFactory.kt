package com.example.download_manager_feature

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navigation.domain.Router

class DownloadViewModelFactory(
    private val context: Context,
    private val downloadRepo: DownloadRepo,
    private val router: Router
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DownloadViewModel::class.java)){
            return DownloadViewModel(DownloadRepo(context), router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}