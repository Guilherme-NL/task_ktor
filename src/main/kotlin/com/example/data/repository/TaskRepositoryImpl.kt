package com.example.data.repository

import com.example.core.domain.data.repository.TaskRepository
import com.example.core.domain.model.Task
import com.example.data.request.UpdateTaskRequest
import com.example.utils.Constants.TASKS_COLLECTION
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class TaskRepositoryImpl(
    database: CoroutineDatabase,
): TaskRepository {

    private val tasksCollection = database.getCollection<Task>(TASKS_COLLECTION)

    override suspend fun getTasks(): List<Task> {
        return tasksCollection.find().toList()
    }

    override suspend fun getTaskById(id: String): Task? {
        return tasksCollection.findOneById(id = id)
    }

    override suspend fun insert(task: Task): Boolean {
        return tasksCollection.insertOne(task).wasAcknowledged()
    }

    override suspend fun update(id: String, updateTaskRequest: UpdateTaskRequest, currentTask: Task): Boolean {
        return tasksCollection.updateOneById(id = id, update = Task(
            id = currentTask.id,
            title = updateTaskRequest.title,
            description = updateTaskRequest.description,
            priority = updateTaskRequest.priority,
            dueDate = currentTask.dueDate,
            completed = currentTask.completed,
            createAt = currentTask.createAt
        )).wasAcknowledged()
    }

    override suspend fun delete(id: String): Boolean {
        return tasksCollection.deleteOneById(id = id).wasAcknowledged()
    }

    override suspend fun completeTask(id: String): Long {
        val updateResult = tasksCollection.updateOne(Task::id eq id, setValue(Task::completed, true))
        return updateResult.modifiedCount
    }
}