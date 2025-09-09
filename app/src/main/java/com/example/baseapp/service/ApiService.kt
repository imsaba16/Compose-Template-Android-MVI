package com.example.baseapp.service

import com.example.baseapp.exception.ApiException
import com.example.baseapp.utils.PreferenceKeys
import com.example.baseapp.utils.PreferenceManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.utils.io.InternalAPI

class ApiService(
    private val clientLazy: Lazy<HttpClient>,
    private val preferenceManager: PreferenceManager,
) {

    companion object {
        const val BASE_URL = "https://catfact.ninja"

    }
    val client: HttpClient get() = clientLazy.value

    fun HttpRequestBuilder.authenticatedJsonRequest(bearer: String) {
        header("Authorization", bearer)
        contentType(ContentType.Application.Json)
    }

    fun endpoint(path: String) = "${BASE_URL}$path"

    suspend fun bearer() =
        "Bearer ${preferenceManager.getValue(PreferenceKeys.ACCESS_TOKEN, "")}"

    suspend inline fun <reified T> get(
        path: String,
        noinline block: HttpRequestBuilder.() -> Unit = {},
    ): T {
        val response = client.get(endpoint(path)) {
            authenticatedJsonRequest(bearer())
            block()
        }
        return handleResponse(response)
    }

    suspend inline fun <reified T> post(
        path: String,
        bodyContent: Any,
    ): T {
        val response = client.post(endpoint(path)) {
            authenticatedJsonRequest(bearer())
            setBody(bodyContent)
        }
        return handleResponse(response)
    }

    suspend inline fun <reified T> put(
        path: String,
        bodyContent: Any,
        noinline block: HttpRequestBuilder.() -> Unit = {},
    ): T {
        val response = client.put(endpoint(path)) {
            authenticatedJsonRequest(bearer())
            setBody(bodyContent)
            block()
        }
        return handleResponse(response)
    }


    suspend inline fun <reified T> delete(
        path: String,
        noinline block: HttpRequestBuilder.() -> Unit = {},
    ): T {
        val response = client.delete(endpoint(path)) {
            authenticatedJsonRequest(bearer())
            block()
        }
        if (response.status.value in 200..299) {
            return response.body()
        } else {
            throw ApiException(response.status.value, "Unexpected status: ${response.status}")
        }
    }

    suspend inline fun <reified T> patch(
        path: String,
        bodyContent: Any? = null,
        noinline block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        val response = client.patch(endpoint(path)) {
            authenticatedJsonRequest(bearer())
            if (bodyContent != null) {
                setBody(bodyContent)
            }
            block()
        }
        return handleResponse(response)
    }


    suspend inline fun <reified T> getPaged(
        path: String,
        page: Int,
        limit: Int,
        familyId: String? = null,
        category: String? = null,
        noinline block: HttpRequestBuilder.() -> Unit = {},
    ): T = get(path) {
        if (familyId != null) {
            parameter("familyId", familyId)
        }
        parameter("page", page)
        parameter("limit", limit)
        if (category != null) {
            parameter("category", category)
        }
        block()
    }

    suspend fun downloadFile(url: String): ByteArray {
        val response = client.get(url)
        return handleResponse(response)
    }

    suspend fun uploadFileByteArray(
        method: HttpMethod,
        fileBytes: ByteArray?,
        imageFieldKey: String,
        physicalLocationKey: String,
        physicalLocation: String,
        fileName: String,
        fileNameKey: String,
        documentName: String,
        serverUrl: String,
        familyId: String?
    ): String {
        val response = client.submitFormWithBinaryData(
            url = serverUrl,
            formData = formData {
                append(fileNameKey, documentName)
                append(physicalLocationKey, physicalLocation)
                if (!familyId.isNullOrBlank()) {
                    append("familyId", familyId)
                }
                if (fileBytes != null) {
                    append(
                        key = imageFieldKey,
                        value = fileBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
                        }
                    )
                }
            }
        ) {
            this.method = method
        }

        return handleResponse(response)
    }


    suspend fun uploadMultipleFilesByteArray(
        serverUrl: String,
        title: String,
        module: String,
        description: String,
        fileName: String,
        fileByteArrayList: ByteArray?,
    ): String {
        val response = client.submitFormWithBinaryData(
            url = serverUrl,
            formData = formData {
                append("title", title)
                append("module", module)
                append("description", description)
                if (fileByteArrayList != null){
                    append("file", fileByteArrayList, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
                    })
                }
            }
        ) {
            this.method = HttpMethod.Post
        }

        return handleResponse(response)
    }


    @OptIn(InternalAPI::class)
    suspend fun uploadProfileImage(
        url: String,
        fileBytes: ByteArray,
        fileName: String,
    ): String {
        val response = client.put("$BASE_URL$url") {
            body = MultiPartFormDataContent(
                formData {
                    append("file", fileBytes, Headers.build {
                        append(HttpHeaders.ContentDisposition, "name=\"file\"; filename=\"$fileName\"")
                        append(HttpHeaders.ContentType, "image/jpeg")
                    })
                }
            )
        }
        return handleResponse(response)
    }


    suspend inline fun <reified T> handleResponse(response: HttpResponse): T {
        if (response.status.value in 200..299) {
            return response.body()
        } else {
            val errorBody = try {
                val jsonText = response.bodyAsText()
                jsonText
            } catch (e: Exception) {
                response.status.description ?: "Something went wrong."
            }
            throw ApiException(response.status.value, errorBody)
        }
    }




}