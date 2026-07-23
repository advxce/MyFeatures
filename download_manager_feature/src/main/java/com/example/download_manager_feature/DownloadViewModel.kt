package com.example.download_manager_feature

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import com.example.download_manager_feature.ServiceLocator.testDownloadList
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class DownloadViewModel(
    private val downloadRepo: DownloadRepo,
    private val router: Router
) : ViewModel() {



    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    val downloadTasks = ConcurrentHashMap<Job, DownloadItem>()

    private val _downloadingFile = MutableStateFlow<List<DownloadItem>>(emptyList())
    val downloadingFile = _downloadingFile.asStateFlow()

    private val systemIdToItemIsMap = HashMap<Long, String>()

    init {
        _downloadingFile.value = testDownloadList
        viewModelScope.launch {
            downloadRepo.downloadCompleteEvent.collect { systemId ->
                val itemId = systemIdToItemIsMap[systemId] ?: return@collect
                val updatedList = _downloadingFile.value.map { item->
                    if(item.id == itemId){
                        item.copy(status = DownloadStatus.COMPLETED, progress = 100)
                    } else{
                        item
                    }
                }
                _downloadingFile.value = updatedList
                testDownloadList.clear()
                testDownloadList.addAll(updatedList)
                systemIdToItemIsMap.remove(systemId)
            }
        }

    }


    fun navigateTo(navAction: NavAction){
        router.navigateTo(navAction)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun downloadFile(downloadItem: DownloadItem, context: Context) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            val systemID = downloadRepo.startDownload(downloadItem.url, downloadItem.fileName)
            systemIdToItemIsMap[systemID] = downloadItem.id
            var isDownloading = true
            while (isDownloading){

                val currentItem = _downloadingFile.value.find { it.id == downloadItem.id }
                if (currentItem?.status == DownloadStatus.COMPLETED) {
                    isDownloading = false
                    break
                }

                val progress = downloadRepo.getDownloadProgress(context, systemID)
                withContext(Dispatchers.Main){
                    val updatedList = _downloadingFile.value.map { item ->
                        if (item.id == downloadItem.id) {
                            if (progress >= 100) {
                                isDownloading = false
                                systemIdToItemIsMap.remove(systemID)
                                return@withContext item.copy(
                                    progress = 100,
                                    status = DownloadStatus.COMPLETED
                                )
                            }
                            println("item if: $item")
                            println("progress: $progress")
                            item.copy(progress = progress, status = DownloadStatus.DOWNLOADING)
                        } else {
                            println("item else: $item")
                            item
                        }
                    }
                    _downloadingFile.value = updatedList

                }

                if (isDownloading){
                    delay(500)
                }

            }
        }
        downloadTasks[job] = downloadItem.copy(status = DownloadStatus.DOWNLOADING)
    }

    fun pauseFile(downloadItem: DownloadItem) {

    }

    override fun onCleared() {
        downloadTasks.forEach { (job, item) ->
            job.cancel()
        }

        downloadRepo.clear()
        viewModelScope.cancel()
        super.onCleared()
    }

}