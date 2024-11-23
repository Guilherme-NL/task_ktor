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
            return SimpleResponse(success = false, message = "Preencha todos os campos")
        }

        val insert = taskRepository.insert(task = createTaskRequest.toTask())
        if (!insert) {
            return SimpleResponse(success = false, message = "Erro ao cadastrar a task", statusCode = 400)
        }
        return SimpleResponse(success = true, message = "Tarefa cadastrada com sucesso", statusCode = 201)
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return taskRepository.getTaskById(taskId)
    }

    override suspend fun update(taskId: String, updateTaskRequest: UpdateTaskRequest): SimpleResponse {
        val task = getTaskById(taskId) ?: return SimpleResponse(success = false, message = "task não encontrada", statusCode = 404)

        val result = validateUpdateTaskRequest(updateTaskRequest)
        if(!result) {
            return SimpleResponse(success = false, message = "Preencha todos os campos")
        }

        return when(taskRepository.update(taskId, updateTaskRequest, task)){
            true -> SimpleResponse(success = true, message = "Tarefa atualizada com sucesso", statusCode = 200)
            false -> SimpleResponse(success = false, message = "Erro ao atulizar tarefa", statusCode = 400)
        }
    }

    override suspend fun delete(taskId: String): SimpleResponse {
        val task = getTaskById(taskId)

        if (task != null){
            return if (taskRepository.delete(task.id)){
                SimpleResponse(success = true, message = "Tarefa deletada com sucesso", statusCode = 200)
            } else{
                SimpleResponse(success = false, message = "Erro ao atualizar a tarefa", statusCode = 400)
            }
        }

        throw TaskNotFoundException("Nenhuma tarefa com o id $taskId, foi encontrada")
    }

    override suspend fun complete(taskId: String): SimpleResponse {
        val task = getTaskById(taskId)
        task?.let { taskFound ->
            val modifiedCount = taskRepository.completeTask(taskFound.id)
            if (modifiedCount > 0){
                return SimpleResponse(success = true, message = "Task concluida com sucesso", statusCode = 200)
            }

            return SimpleResponse(success = false, message = "Erro ao concluir tarefa", statusCode = 400)
        } ?: throw TaskNotFoundException("Nenhuma tarefa com o id $taskId, foi encontrada")
    }
}