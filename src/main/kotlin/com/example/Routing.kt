package com.example

import com.example.core.domain.data.service.TaskService
import com.example.routes.taskRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val taskService by inject<TaskService>()

    routing {
        taskRoute(taskService)
    }
}
