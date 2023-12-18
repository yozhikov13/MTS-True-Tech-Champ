package com.codenrock.cloud.service.resource.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PutResource(
    val id: Int,
    val cpu: Int,
    val ram: Int,
)
