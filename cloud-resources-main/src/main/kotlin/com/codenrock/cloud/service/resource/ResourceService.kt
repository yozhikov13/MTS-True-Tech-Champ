package com.codenrock.cloud.service.resource

import com.codenrock.cloud.service.price.PriceClient
import com.codenrock.cloud.service.price.model.Price
import com.codenrock.cloud.service.resource.model.GetResource
import com.codenrock.cloud.service.resource.model.PostResource
import com.codenrock.cloud.service.resource.model.PutResource
import com.codenrock.cloud.service.resource.model.ResourceType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

@Service
class ResourceService(
    private val resourceClient: ResourceClient,
    private val priceClient: PriceClient,
): InitializingBean {

    lateinit var minDb: Price
    lateinit var minVm: Price

    suspend fun init()  {
        add(ResourceType.DB, minDb)
        add(ResourceType.VM, minVm)
    }

    suspend fun get() = resourceClient.get()

    suspend fun add(type: ResourceType, pod: Price) {
        when (type) {
            ResourceType.VM -> resourceClient.post(PostResource(pod.cpu, pod.ram, ResourceType.VM))
            ResourceType.DB -> resourceClient.post(PostResource(pod.cpu, pod.ram, ResourceType.DB))
        }
    }

    suspend fun update(pod: GetResource, upPod: Price) {
        resourceClient.put(PutResource(pod.id, upPod.cpu, upPod.ram))

    }

    suspend fun delete(type: ResourceType) {
        resourceClient.get()
            .first { it.type == type }
            .let { resourceClient.delete(it.id) }
    }

    suspend fun deletePod(pod: GetResource) {
        resourceClient.get()
            .let { resourceClient.delete(pod.id) }
    }

    override fun afterPropertiesSet() {
        runBlocking  {
            resourceClient.get()
                .forEach {
                    resourceClient.delete(it.id)
                }
            minDb = priceClient.prices
                .getValue(ResourceType.DB).minByOrNull { it.cost }!!
            minVm = priceClient.prices
                .getValue(ResourceType.VM).minByOrNull { it.cost }!!
        }
    }


}
