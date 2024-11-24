package com.example.data.service

import com.example.TaskNotFoundException
import com.example.core.domain.data.repository.TaskRepository
import com.example.core.domain.data.service.TaskService
import com.example.core.domain.model.Task
import com.example.core.domain.usecase.ValidateCreateTaskRequest
import com.example.core.domain.usecase.ValidateUpdateTaskRequest
import com.example.data.request.CreateTaskRequest
import com.example.data.request.UpdateTaskRequest
import com.example.data.request.toTask
import com.example.data.response.SimpleResponse
import com.example.utils.ErrorCodes
import com.example.utils.SuccessCodes

class TaskServiceImpl constructor(
    private val taskRepository: TaskRepository,
    private val validateCreateTaskRequest: ValidateCreateTaskRequest,
    private val validateUpdateTaskRequest: ValidateUpdateTaskRequest
): TaskService {
    override suspend fun getTasks(): List<Task> {
        return taskRepository.getTasks()
    }

    override suspend fun insert(createTaskRequest: CreateTaskRequest): SimpleResponse {
        val result = validateCreateTaskRequest(createTaskRequest)
        if (!result) {
            return SimpleResponse(success = false, message = ErrorCodes.EMPTY_FIELDS.message)
        }

        val insert = taskRepository.insert(task = createTaskRequest.toTask())
        if (!insert) {
            return SimpleResponse(success = false, message = ErrorCodes.REGISTER_TASK.message, statusCode = 400)
        }
        return SimpleResponse(success = true, message = SuccessCodes.REGISTER_TASK.message, statusCode = 201)
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return taskRepository.getTaskById(taskId)
    }

    override suspend fun update(taskId: String, updateTaskRequest: UpdateTaskRequest): SimpleResponse {
        val task = getTaskById(taskId) ?: throw TaskNotFoundException(ErrorCodes.TASK_NOT_FOUND.message)

        val result = validateUpdateTaskRequest(updateTaskRequest)
        if(!result) {
            return SimpleResponse(success = false, message = ErrorCodes.EMPTY_FIELDS.message)
        }

        return when(taskRepository.update(taskId, updateTaskRequest, task)){
            true -> SimpleResponse(success = true, message = SuccessCodes.UPDATE_TASK.message, statusCode = 200)
            false -> SimpleResponse(success = false, message = ErrorCodes.UPDATE_TASK.message, statusCode = 400)
        }
    }

    override suspend fun delete(taskId: String): SimpleResponse {
        val task = getTaskById(taskId)

        if (task != null){
            return if (taskRepository.delete(task.id)){
                SimpleResponse(success = true, message = SuccessCodes.DELETE_TASK.message, statusCode = 200)
            } else{
                SimpleResponse(success = false, message = ErrorCodes.DELETE_TASK.message, statusCode = 400)
            }
        }

        throw TaskNotFoundException(ErrorCodes.TASK_NOT_FOUND.message)
    }

    override suspend fun complete(taskId: String): SimpleResponse {
        val task = getTaskById(taskId)
        task?.let { taskFound ->
            val modifiedCount = taskRepository.completeTask(taskFound.id)
            if (modifiedCount > 0){
                return SimpleResponse(success = true, message = SuccessCodes.COMPLETE_TASK.message, statusCode = 200)
            }

            return SimpleResponse(success = false, message = ErrorCodes.COMPLETE_TASK.message, statusCode = 400)
        } ?: throw TaskNotFoundException(ErrorCodes.TASK_NOT_FOUND.message)
    }
}