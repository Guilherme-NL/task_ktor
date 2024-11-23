package com.example.core.domain.data.repository

import com.example.core.domain.model.Task
import com.example.data.request.UpdateTaskRequest

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun getTaskById(id: String): Task?
    suspend fun insert(task: Task): Boolean
    suspend fun update(id: String, updateTaskRequest: UpdateTaskRequest, currentTask: Task): Boolean
    suspend fun delete(id: String): Boolean
    suspend fun completeTask(id: String): Long
}