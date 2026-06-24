package com.example.core.network

data class NetworkConfig(
    val baseUrl: String,
    val isDebug: Boolean,
    val connectTimeoutSeconds: Long = 30L,
    val readTimeoutSeconds: Long    = 30L,
    val writeTimeoutSeconds: Long   = 30L,
    val dynamicHeaders: () -> Map<String, String> = { emptyMap() }
)
