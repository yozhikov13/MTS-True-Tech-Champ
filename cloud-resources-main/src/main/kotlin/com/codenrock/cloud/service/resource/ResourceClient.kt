package com.codenrock.cloud.service.resource

import com.codenrock.cloud.configuration.ApplicationProperties
import com.codenrock.cloud.configuration.WebClientConfiguration.Companion.QUERY_TOKEN
import com.codenrock.cloud.service.resource.model.GetResource
import com.codenrock.cloud.service.resource.model.PostResource
import com.codenrock.cloud.service.resource.model.PutResource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBody

@Service
class ResourceClient(
    private val applicationProperties: ApplicationProperties,
    private val cloudWebClient: WebClient,
) {

    companion object {
        private const val PATH = "/resource"
    }

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun get(): List<GetResource> {
        log.debug("#get:")
        return cloudWebClient.get()
            .uri {
                it.path(PATH)
                    .queryParam(QUERY_TOKEN, applicationProperties.token)
                    .build()
            }
            .retrieve()
            .awaitBody()
    }

    suspend fun post(resource: PostResource) {
        log.debug("#post: {}", resource)
        cloudWebClient.post()
            .uri {
                it.path(PATH)
                    .queryParam(QUERY_TOKEN, applicationProperties.token)
                    .build()
            }
            .bodyValue(resource)
            .retrieve()
            .awaitBodilessEntity()
    }

    suspend fun delete(id: Int) {
        log.debug("#delete: {}", id)
        cloudWebClient.delete()
            .uri {
                it.path("$PATH/{id}")
                    .queryParam(QUERY_TOKEN, applicationProperties.token)
                    .build(id)
            }
            .retrieve()
            .awaitBodilessEntity()
    }

    suspend fun put(resource: PutResource) {
        log.debug("#update: {}", resource)
        cloudWebClient.put()
            .uri {
                it.path(PATH)
                    .queryParam(QUERY_TOKEN, applicationProperties.token)
                    .build()
            }
            .bodyValue(resource)
            .retrieve()
            .awaitBodilessEntity()
    }

}