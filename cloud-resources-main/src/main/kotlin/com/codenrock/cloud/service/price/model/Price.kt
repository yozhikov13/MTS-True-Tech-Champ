package com.codenrock.cloud.service.price.model

import com.codenrock.cloud.service.resource.model.ResourceType

data class Price(
    val cost: Int,
    val cpu: Int,
    val id: Int,
    val name: String,
    val ram: Int,
    val type: ResourceType,
)

