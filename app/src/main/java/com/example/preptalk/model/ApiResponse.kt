package com.example.preptalk.model

data class ApiResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ApiMessage
)