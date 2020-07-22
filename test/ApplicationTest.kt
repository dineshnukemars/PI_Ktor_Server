package com.sky.pi

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/destroy").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("cleared pi", response.content)
            }
        }
    }
}
