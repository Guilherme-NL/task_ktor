package com.example.routes

import com.example.core.domain.data.service.TaskService
import com.example.data.request.CreateTaskRequest
import com.example.data.request.UpdateTaskRequest
import com.example.data.response.SimpleResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.taskRoute(taskService: TaskService){
    route("api/tasks/v1"){
        getTasks(taskService)
        insertTask(taskService)
        getTaskById(taskService)
        updateTask(taskService)
        deleteTask(taskService)
        completeTask(taskService)
    }
}

private fun Route.getTasks(taskService: TaskService) {
    get {
        try {
            val tasks = taskService.getTasks()
            call.respond(HttpStatusCode.OK, tasks)
        } catch (ex: Exception) {
            application.log.error(ex.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

private fun Route.getTaskById(taskService: TaskService) {
    get("/{id}" ) {
        val taskId = call.parameters["id"] ?: ""
        val task = taskService.getTaskById(taskId)
        task?.let { safeTask ->
            call.respond(HttpStatusCode.OK, safeTask)
        } ?: call.respond(HttpStatusCode.NotFound, SimpleResponse(success = false, message = "Task não encontrada"))
    }
}

private fun Route.insertTask(taskService: TaskService) {
    post {
       val request = call.receiveNullable<CreateTaskRequest>()

        request?.let { createTaskRequest ->
            val simpleResponse = taskService.insert(createTaskRequest)
            when {
                simpleResponse.success -> {
                    call.respond(HttpStatusCode.Created, simpleResponse)
                }
                simpleResponse.statusCode == 400 -> {
                    call.respond(HttpStatusCode.BadRequest, simpleResponse)
                }
            }
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}

private fun Route.updateTask(taskService: TaskService){
    put("/{id}") {
        val taskId = call.parameters["id"] ?: ""
        val request = call.receiveNullable<UpdateTaskRequest>()
        request?.let { updateTaskRequest ->
            val simpleResponse = taskService.update(taskId, updateTaskRequest)
            when {
                simpleResponse.success -> {
                    call.respond(HttpStatusCode.OK, simpleResponse)
                }
                simpleResponse.statusCode == 404 -> {
                    call.respond(HttpStatusCode.NotFound, simpleResponse)
                }
                else -> {
                    call.respond(HttpStatusCode.BadRequest, simpleResponse)
                }
            }
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}

private fun Route.deleteTask(taskService: TaskService) {
    delete("/{id}") {
        val taskId = call.parameters["id"] ?: ""
        val simpleResponse = taskService.delete(taskId)

        if (simpleResponse.success){
            call.respond(HttpStatusCode.OK, simpleResponse)
        }

        call.respond(HttpStatusCode.BadRequest, simpleResponse)
    }
}

private fun Route.completeTask(taskService: TaskService) {
    patch("/{id}") {
        val taskId = call.parameters["id"] ?: ""
        val simpleResponse = taskService.complete(taskId)

        when{
            simpleResponse.success ->{
                call.respond(HttpStatusCode.OK, simpleResponse)
            }
            else -> call.respond(HttpStatusCode.BadRequest, simpleResponse)
        }
    }
}
