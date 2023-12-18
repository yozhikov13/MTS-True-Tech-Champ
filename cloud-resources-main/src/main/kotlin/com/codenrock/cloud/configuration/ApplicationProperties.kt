package com.codenrock.cloud.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "application")
data class ApplicationProperties
@ConstructorBinding
constructor(
    val baseUrl: String,
    val token: String,
    val cpuLoadMax: Int,
    val memoryLoadMax: Int,
    val delta: Int,
)
