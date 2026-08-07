package com.example.remoteConfig

interface AppConfig {

    fun isFeatureEnabled(feature: String): Boolean

}