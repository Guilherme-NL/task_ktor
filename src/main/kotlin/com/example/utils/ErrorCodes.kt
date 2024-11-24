package com.example.utils

enum class ErrorCodes(val message: String) {
    REGISTER_TASK("Erro ao cadastrar a tarefa"),
    UPDATE_TASK("Erro ao etualizar a tarefa"),
    DELETE_TASK("Erro ao deletar a tarefa"),
    TASK_NOT_FOUND("Nenhuma tarefa com este id encontrada"),
    EMPTY_FIELDS("Preencha todos os campos"),
    COMPLETE_TASK("Erro ao concluir a tarefa")
}