import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.myitschool.work.core.Constants.SERVER_ADDRESS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.myitschool.work.ApiService

object RetrofitClient {
//    val baseUrl = "https://wizard-world-api.herokuapp.com"
    val baseUrl = "http://172.19.0.1:8090"

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val apiInterface = getInstance().create(ApiService::class.java)
}