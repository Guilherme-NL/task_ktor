package com.example

import com.example.di.databaseModule
import com.example.di.repositoryModule
import com.example.di.serviceModule
import com.example.di.useCaseModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(
            databaseModule,
            repositoryModule,
            serviceModule,
            useCaseModule
        )
    }

    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()
    configureStatusPage()
}
