package com.sekota

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform