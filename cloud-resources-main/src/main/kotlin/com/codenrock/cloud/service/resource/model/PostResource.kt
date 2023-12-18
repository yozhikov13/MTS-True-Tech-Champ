package com.codenrock.cloud.service.resource.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PostResource(
    val cpu: Int,
    val ram: Int,
    val type: ResourceType,
)
