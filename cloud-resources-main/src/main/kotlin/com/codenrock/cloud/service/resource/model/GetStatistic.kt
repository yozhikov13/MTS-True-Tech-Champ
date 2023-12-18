package com.codenrock.cloud.service.resource.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GetStatistic(
    val availability: Float,
    @JsonProperty("cost_total")
    val costTotal: Float,
    @JsonProperty("db_cpu")
    val dbCpu: Int,
    @JsonProperty("db_cpu_load")
    val dbCpuLoad: Float,
    @JsonProperty("db_ram")
    val dbRam: Int,
    @JsonProperty("db_ram_load")
    val dbRamLoad: Float,
    val last1: Int,
    val last15: Int,
    val last5: Int,
    val lastDay: Int,
    val lastHour: Int,
    val lastWeek: Int,
    @JsonProperty("offline_time")
    val offlineTime: Int,
    val online: Boolean,
    @JsonProperty("online_time")
    val onlineTime: Int,
    val requests: Int,
    @JsonProperty("requests_total")
    val requestsTotal: Float,
    @JsonProperty("response_time")
    val responseTime: Int,
    @JsonProperty("user_name")
    val userName: String,
    @JsonProperty("vm_cpu")
    val vmCpu: Int,
    @JsonProperty("vm_cpu_load")
    val vmCpuLoad: Float,
    @JsonProperty("vm_ram")
    val vmRam: Int,
    @JsonProperty("vm_ram_load")
    val vmRamLoad: Float,
)
