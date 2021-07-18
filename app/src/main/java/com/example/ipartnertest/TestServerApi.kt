package com.example.ipartnertest

import com.example.ipartnertest.data.EntriesResponse
import com.example.ipartnertest.data.Response
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

private const val TOKEN = "w8cNdIR-iH-yvrj5RH"

//Using http instead of https because otherwise we receive certificate exception
private const val BASE_URL = "http://bnet.i-partner.ru/"

val interceptor = HttpLoggingInterceptor()
val client = OkHttpClient.Builder()
    .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface TestServerApi {

    @FormUrlEncoded
    @Headers("token: $TOKEN")
    @POST("testAPI/")
    suspend fun newSession(@Field("a") command: String = "new_session") : Response

    @FormUrlEncoded
    @Headers("token: $TOKEN")
    @POST("testAPI/")
    suspend fun getEntries(
        @Field("a") command: String = "get_entries",
        @Field("session") session: String
    ) : EntriesResponse

    @FormUrlEncoded
    @Headers("token: $TOKEN")
    @POST("testAPI/")
    suspend fun addEntry(
        @Field("a") command: String = "add_entry",
        @Field("session") session: String,
        @Field("body") body: String
    ) : Response
}

object TestServerService {
    val retrofitService : TestServerApi by lazy {
        retrofit.create(TestServerApi::class.java)
    }
}