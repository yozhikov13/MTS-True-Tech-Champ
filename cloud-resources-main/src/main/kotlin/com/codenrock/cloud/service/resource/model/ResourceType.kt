package com.codenrock.cloud.service.resource.model

import com.fasterxml.jackson.annotation.JsonProperty

enum class ResourceType {
    @JsonProperty("db")
    DB,
    @JsonProperty("vm")
    VM;
}