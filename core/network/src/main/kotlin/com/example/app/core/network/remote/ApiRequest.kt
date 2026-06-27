package com.example.app.core.network.remote

enum class HttpMethod { GET, POST, PUT, PATCH, DELETE }

data class ApiRequest(
    val endpoint: String,
    val method: HttpMethod = HttpMethod.GET,
    val query: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
    val body: Any? = null,
    val fields: Map<String, String> = emptyMap(),
    val formUrlEncoded: Boolean = false
) {
    companion object {
        fun get(
            endpoint: String,
            query: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
        ) = ApiRequest(endpoint, HttpMethod.GET, query, headers)

        fun delete(
            endpoint: String,
            query: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
        ) = ApiRequest(endpoint, HttpMethod.DELETE, query, headers)

        fun post(
            endpoint: String,
            body: Any? = null,
            query: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
        ) = ApiRequest(endpoint, HttpMethod.POST, query, headers, body = body)

        fun put(
            endpoint: String,
            body: Any? = null,
            query: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
        ) = ApiRequest(endpoint, HttpMethod.PUT, query, headers, body = body)

        fun patch(
            endpoint: String,
            body: Any? = null,
            query: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
        ) = ApiRequest(endpoint, HttpMethod.PATCH, query, headers, body = body)

        fun form(
            endpoint: String,
            fields: Map<String, String>,
            method: HttpMethod = HttpMethod.POST,
            query: Map<String, String> = emptyMap(),
            headers: Map<String, String> = emptyMap()
        ) = ApiRequest(endpoint, method, query, headers, fields = fields, formUrlEncoded = true)
    }
}
