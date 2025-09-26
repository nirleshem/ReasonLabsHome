package com.example.nir

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform