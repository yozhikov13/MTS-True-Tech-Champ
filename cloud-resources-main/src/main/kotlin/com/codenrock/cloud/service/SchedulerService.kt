package com.codenrock.cloud.service

import com.codenrock.cloud.configuration.ApplicationProperties
import com.codenrock.cloud.service.price.PriceClient
import com.codenrock.cloud.service.resource.ResourceService
import com.codenrock.cloud.service.resource.model.GetResource
import com.codenrock.cloud.service.resource.model.ResourceType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SchedulerService(
    private val priceClient: PriceClient,
    private val resourceService: ResourceService,
    private val applicationProperties: ApplicationProperties,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRateString = "\${application.schedule-rate}", timeUnit = TimeUnit.SECONDS, initialDelay = 0)
    fun task() {
        log.debug("#task: start")
        calculate()
    }

    private fun calculate() =
        runBlocking {
            val currentResources = resourceService.get()
            when (currentResources.isEmpty()) {
                true -> resourceService.init()
                else -> update(currentResources)
            }
        }
    private suspend fun update(currentResources: List<GetResource>) {
        val resources = currentResources.groupBy { it.type }
        updateByType(ResourceType.VM, resources)
        updateByType(ResourceType.DB, resources)

    }

    private suspend fun updateByType(type: ResourceType, resources: Map<ResourceType, List<GetResource>>) {
        val typedResources = resources.getValue(type)
        val cpuLoad = typedResources.map { it.cpuLoad }.sum()
        val ramLoad = typedResources.map { it.ramLoad }.sum()
        val failedErr = typedResources.map { it.failed }
        val cpuCount = typedResources.sumOf { it.cpu }
        val ramCount = typedResources.sumOf { it.ram }
        val podCount = typedResources.size
        val cpuLoadAverage = cpuLoad / cpuCount
        val ramLoadAverage = ramLoad / ramCount

        log.debug("--------------------------------------------------------------------------------------")
        log.debug("#updateByType: type = [$type], pod count = [$podCount], " +
                "cpu load = [$cpuLoad], ram load = [$ramLoad], failed = [$failedErr]")
        log.debug("-----------------------------------")
        log.debug("type = [$type], cpuLoadAverage = [$cpuLoadAverage], ramLoadAverage = [$ramLoadAverage]")
        log.debug("--------------------------------------------------------------------------------------")

        val cpuAllNeed = cpuLoad/applicationProperties.cpuLoadMax
        val ramAllNeed = ramLoad/applicationProperties.memoryLoadMax

        val cpuNeedNow = cpuAllNeed - cpuCount
        val ramNeedNow = ramAllNeed - ramCount

        log.debug("***********")
        log.debug("type = [$type], cpuNeedNow = [$cpuNeedNow], ramNeedNow = [$ramNeedNow], cpuCount = [$cpuCount], ramCount = [$ramCount]")
        log.debug("***********")

        if (cpuLoadAverage > applicationProperties.cpuLoadMax ||
            ramLoadAverage > applicationProperties.memoryLoadMax ) {

            var check = false



            var newPod = priceClient.prices.getValue(type).minByOrNull { it.cost }!!

            for (pr in priceClient.prices.getValue(type)) {
                if (cpuNeedNow >= pr.cpu && ramNeedNow >= pr.ram) {
                    newPod = pr
                }
            }
            log.debug("#updateByType: add = [$type], cpu = [${newPod.cpu}], ram = [${newPod.ram}]")
            resourceService.add(type, newPod)
        }

        if (podCount > 1 &&
            (cpuLoad / (cpuCount - 1) < applicationProperties.cpuLoadMax.minus(applicationProperties.delta) &&
            ramLoad / (ramCount - 1) < applicationProperties.memoryLoadMax.minus(applicationProperties.delta)) ) {

        //if (podCount > 1 && cpuNeedNow < 0 && ramNeedNow < 0){

            val cpuDel = Math.abs(cpuNeedNow) - 1
            val ramDel = Math.abs(ramNeedNow) - 1

            var delPod = typedResources.minByOrNull { it.cost }!!

            for(pr in typedResources){
                if(cpuDel >= pr.cpu && ramDel >= pr.ram){
                    delPod = pr
                }
            }

            log.debug("#updateByType: deletePod = [$type], cpu = [${delPod.cpu}], ram = [${delPod.ram}]")
            resourceService.deletePod(delPod)
        }

    }
}
