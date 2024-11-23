package com.example.core.domain.usecase

import com.example.data.request.CreateTaskRequest

interface ValidateCreateTaskRequest {
    operator fun invoke(request: CreateTaskRequest): Boolean
}

class ValidateCreateTaskRequestImpl: ValidateCreateTaskRequest{
    override fun invoke(request: CreateTaskRequest): Boolean {
        return !(request.title.isEmpty() || request.description.isEmpty() || request.priority.isEmpty() || request.dueDate.isEmpty())
    }
}