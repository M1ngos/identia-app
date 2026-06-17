package com.identia.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform