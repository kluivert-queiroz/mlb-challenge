package io.kotest.provided

import com.util.TestDbContainer
import io.kotest.core.config.AbstractProjectConfig
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension

object ProjectConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(MicronautKotest5Extension)
    override fun beforeAll() {
        TestDbContainer.start()
    }

    override fun afterAll() {
        TestDbContainer.stop()
    }
}
