package com.example.baseapp.exception

class ApiException(
    val code: Int,
    override val message: String
) : Exception(message)