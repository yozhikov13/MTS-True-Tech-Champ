package com.codenrock.cloud.service.resource.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GetResource(
    val id: Int,
    val cost: Int,
    val cpu: Int,
    @JsonProperty("cpu_load")
    val cpuLoad: Float,
    val failed: Boolean,
    val ram: Int,
    @JsonProperty("ram_load")
    val ramLoad: Float,
    val type: ResourceType,
)
