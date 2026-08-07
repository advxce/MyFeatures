package com.example.myfeatures

import android.app.Application
import android.util.Log
import com.example.download_manager_feature.DownloadRepo
import com.example.remoteConfig.AppConfig
import com.example.remoteConfig.FirebaseAppConfig
import com.example.remoteConfig.RemoteConfigProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class App: Application(), RemoteConfigProvider {

    private val firebaseRemoteConfig get() = FirebaseRemoteConfig.getInstance()

    override val appConfig: AppConfig by lazy { FirebaseAppConfig( firebaseRemoteConfig)}

    override fun onCreate() {
        super.onCreate()
        setupAndFetchConfig()

    }

    private fun setupAndFetchConfig(){
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("RemoteConfig", "Конфиг успешно обновлен: $updated")
            } else {
                Log.e("RemoteConfig", "Ошибка загрузки конфига")
            }
        }
    }

}