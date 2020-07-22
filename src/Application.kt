package com.sky.pi

import com.pi4j.io.gpio.PinState
import com.sky.pi.com.sky.pi.PiAccess
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ShutDownUrl
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    install(ShutDownUrl.ApplicationCallFeature) {
        // The URL that will be intercepted
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

    routing {

        val piAccess: PiAccess = object : PiAccess {}

        get("/create") {
            piAccess.create()
            call.respondText("initiated pi", contentType = ContentType.Text.Plain)
        }

        get("/setDigiOutState") {

            val pin: Int? = call.request.queryParameters["pin"]?.toInt()
            val state: Boolean? = call.request.queryParameters["state"]?.toBoolean()

            if (pin == null || state == null) {
                call.respondText(
                    "please supply pin number and state(true or false)",
                    contentType = ContentType.Text.Plain
                )
                return@get
            }

            val pinState = if (state) PinState.HIGH else PinState.LOW
            piAccess.setPinDigitalOut(pin, pinState)

            call.respondText("pinstate set to $state", contentType = ContentType.Text.Plain)
        }

        get("/destroy") {
            piAccess.destroy()
            call.respondText("cleared pi", contentType = ContentType.Text.Plain)
        }

        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

