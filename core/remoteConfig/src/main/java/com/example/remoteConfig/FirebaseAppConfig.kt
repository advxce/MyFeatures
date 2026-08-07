package com.example.remoteConfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class FirebaseAppConfig(
    private val remoteConfig: FirebaseRemoteConfig
): AppConfig {
    override fun isFeatureEnabled(feature: String): Boolean {
        return remoteConfig.getBoolean(feature)
    }

}