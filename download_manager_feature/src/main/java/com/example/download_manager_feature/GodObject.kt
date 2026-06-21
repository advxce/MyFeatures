package com.example.download_manager_feature

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

object ServiceLocator {

    private var repository: DownloadRepo? = null
    private var retrofit: Retrofit? = null
    val testDownloadList = mutableListOf(
        DownloadItem(
            id = "1",
            fileName = "Основы архитектуры.mp4",
            progress = 0,
            status = DownloadStatus.IDLE,
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
        ),
        DownloadItem(
            id = "2",
            fileName = "Введение в Корутины.mp3",
            progress = 0,
            status = DownloadStatus.IDLE,
            url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        ),
        DownloadItem(
            id = "3",
            fileName = "Методичка по SOLID.pdf",
            progress = 0,
            status = DownloadStatus.IDLE,
            url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        )
    )

    fun provideRepository(context: Context): DownloadRepo {
        return repository ?: synchronized(this) {
            repository ?: DownloadRepo(context.applicationContext).also {
                repository = it
            }
        }
    }

    val okHttpClient = OkHttpClient.Builder().build()

    fun provideRetrofit(): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl("https://www.w3.org")
                .client(okHttpClient)
                .build().also {
                    retrofit = it
                }
        }
    }

    val fileDownloadApi = provideRetrofit().create<FileDownloadApi>(FileDownloadApi::class.java)
}