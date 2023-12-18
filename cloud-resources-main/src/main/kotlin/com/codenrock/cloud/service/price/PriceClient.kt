package com.codenrock.cloud.service.price

import com.codenrock.cloud.service.price.model.Price
import com.codenrock.cloud.service.resource.model.ResourceType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class PriceClient(
    private val cloudWebClient: WebClient,
): InitializingBean {

    companion object {
        private const val PRICE_URL = "/price"
    }

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    lateinit var prices: Map<ResourceType, List<Price>>

    suspend fun get(): List<Price> {
        log.debug("#get:")
        return cloudWebClient.get()
            .uri(PRICE_URL)
            .retrieve()
            .awaitBody()
    }

    override fun afterPropertiesSet() = runBlocking {
            prices = get().groupBy({it.type}, {it})
        }

}