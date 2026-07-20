package com.example.navigation.domain


interface Router {
    fun navigateTo(navAction: NavAction)
    fun back()
}
