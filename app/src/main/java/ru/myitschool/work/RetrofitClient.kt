import ru.myitschool.work.core.Constants.SERVER_ADDRESS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.myitschool.work.ApiService

object RetrofitClient {
    val baseUrl = SERVER_ADDRESS

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}