package com.example.di

import com.example.core.domain.data.repository.TaskRepository
import com.example.core.domain.data.service.TaskService
import com.example.core.domain.usecase.ValidateCreateTaskRequest
import com.example.core.domain.usecase.ValidateCreateTaskRequestImpl
import com.example.core.domain.usecase.ValidateUpdateTaskRequest
import com.example.core.domain.usecase.ValidateUpdateTaskRequestImpl
import com.example.data.repository.TaskRepositoryImpl
import com.example.data.service.TaskServiceImpl
import com.example.utils.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        val client = KMongo.createClient(connectionString = Constants.MONGODB_URI_LOCAL).coroutine
        client.getDatabase(Constants.LOCAL_DATABASE)
    }
}

val repositoryModule = module {
    single<TaskRepository> {
        TaskRepositoryImpl(get())
    }
}

val serviceModule = module {
    single<TaskService> {
        TaskServiceImpl(get(), get(), get())
    }
}

val useCaseModule = module {
    single<ValidateCreateTaskRequest> {
        ValidateCreateTaskRequestImpl()
    }
    single<ValidateUpdateTaskRequest> {
        ValidateUpdateTaskRequestImpl()
    }
}