package com.codenrock.cloud.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration(proxyBeanMethods = false)
class WebClientConfiguration(
    private val applicationProperties: ApplicationProperties,
) {

    companion object {
        const val QUERY_TOKEN = "token"
    }

    @Bean
    fun cloudWebClient(): WebClient =
        WebClient.builder()
            .baseUrl(applicationProperties.baseUrl)
            .build()

}