package com.example.download_manager_feature

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileDownloadApi {

    @Streaming
    @GET
    suspend fun downloadFile(
        @Url fileUrl: String,
        @Header("Range") range: String? = null
    ): Response<ResponseBody>
}