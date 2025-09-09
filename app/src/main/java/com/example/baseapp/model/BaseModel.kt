package com.example.baseapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseModel(
    @SerialName("fact")
    val fact: String,
    @SerialName("length")
    val length: Int
)
