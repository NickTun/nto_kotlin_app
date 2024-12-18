package ru.myitschool.work
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ApiService {
    @GET("/api/{LOGIN}/auth")
    suspend fun auth(@Path("LOGIN") login: String): ResponseBody

    @GET("/api/{LOGIN}/info")
    suspend fun info(@Path("LOGIN") login: String): ResponseBody

    @PATCH("/api/{LOGIN}/open")
    suspend fun open(@Body data: Data, @Path("LOGIN") login: String): ResponseBody
}