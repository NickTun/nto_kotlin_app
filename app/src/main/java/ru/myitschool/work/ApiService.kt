package ru.myitschool.work
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/{LOGIN}/auth")
    suspend fun auth(@Path("LOGIN") login: String): ResponseBody

    @GET("/{LOGIN}/info")
    suspend fun info(@Path("LOGIN") login: String): ResponseBody
}