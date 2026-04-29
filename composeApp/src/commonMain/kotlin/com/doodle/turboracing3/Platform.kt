package com.doodle.turboracing3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform