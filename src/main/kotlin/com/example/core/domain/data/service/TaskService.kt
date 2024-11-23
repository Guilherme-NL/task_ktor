package com.example.core.domain.data.service

import com.example.core.domain.model.Task
import com.example.data.request.CreateTaskRequest
import com.example.data.request.UpdateTaskRequest
import com.example.data.response.SimpleResponse

interface TaskService {
    suspend fun getTasks(): List<Task>
    suspend fun insert(createTaskRequest: CreateTaskRequest): SimpleResponse
    suspend fun getTaskById(taskId: String): Task?
    suspend fun update(taskId: String, updateTaskRequest: UpdateTaskRequest): SimpleResponse
    suspend fun delete(taskId: String): SimpleResponse
    suspend fun complete(taskId: String): SimpleResponse
}