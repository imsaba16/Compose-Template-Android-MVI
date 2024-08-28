package com.example.baseapp.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseModel(val name: String)
