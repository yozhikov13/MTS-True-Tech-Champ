package com.codenrock.cloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CloudApplication

fun main(args: Array<String>) {
	runApplication<CloudApplication>(*args)
}
