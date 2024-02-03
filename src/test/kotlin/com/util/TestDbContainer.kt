package com.util

import org.testcontainers.containers.MySQLContainer

class TestDbContainer : MySQLContainer<TestDbContainer>("mysql:8.1"){
    companion object {
        private lateinit var instance: TestDbContainer

        fun start() {
            if (!Companion::instance.isInitialized) {
                instance = TestDbContainer()
                instance.start()

                // Micronaut will use these when it starts
                System.setProperty("datasources.default.url", instance.jdbcUrl)
                System.setProperty("datasources.default.username", instance.username)
                System.setProperty("datasources.default.password", instance.password)
            }
        }

        fun stop() {
            instance.stop()
        }
    }
}