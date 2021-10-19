package smsinfosolutions.ind.hibl.utilities

/**
 * Created by Manish on 27-Jul-20.
 */


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {
 private val logging= HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
 private const val  url= "https://apppro.hibl.co.in:4433/api/index_up.php/"
 //private const val  url= "https://apppro.hibl.co.in:4433/api_stg/index.php/"



    private val client = OkHttpClient.Builder().addInterceptor(logging).build()
    var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)

        .build()

    fun <T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }



}