package com.it.xevai60.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;



    public static Retrofit getClient(String baseUrl) {
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                //.setLevel(HttpLoggingInterceptor.Level.BASIC)
                .setLevel(HttpLoggingInterceptor.Level.BODY)
                //.setLevel(HttpLoggingInterceptor.Level.HEADERS)
                ;

        if (retrofit==null) {

//            them gia trij vao header cho toan bo request
//            Interceptor clientInterceptor = chain -> {
//                Request request = chain.request();
//                HttpUrl url = request.url().newBuilder().addQueryParameter("name", "value").build();
//                request = request.newBuilder().url(url).build();
//                return chain.proceed(request);
//            };
            OkHttpClient client = new OkHttpClient.Builder()
                    //.addNetworkInterceptor(clientInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000,TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
