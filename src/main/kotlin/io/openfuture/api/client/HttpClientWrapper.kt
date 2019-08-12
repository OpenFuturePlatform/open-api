package io.openfuture.api.client

interface HttpClientWrapper {

    fun get(path: String, headers: Map<String, String>, params: Map<String, String> = mapOf()): HttpResponse

    fun post(path: String, headers: Map<String, String>, body: Any, params: Map<String, String> = mapOf()): HttpResponse

    fun put(path: String, headers: Map<String, String>, body: Any, params: Map<String, String> = mapOf()): HttpResponse

    fun delete(path: String, headers: Map<String, String>, params: Map<String, String> = mapOf()): HttpResponse

}
