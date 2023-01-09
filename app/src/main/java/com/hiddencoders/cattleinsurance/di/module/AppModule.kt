package com.hiddencoders.cattleinsurance.di.module

import android.content.Context
import com.hiddencoders.cattleinsurance.BuildConfig
import com.hiddencoders.cattleinsurance.app.App
import com.hiddencoders.cattleinsurance.data.remote.ApiServices
import com.hiddencoders.cattleinsurance.ui.utilis.sharedPreferences.UserSession
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext context: Context): App {
        return context as App
    }

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext ctx: Context): UserSession {
        return UserSession(ctx)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideClient(interceptor: HttpLoggingInterceptor, prefs: UserSession): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                // Add Auth Header
                val token: String = prefs.getAccessToken().toString()
                val request: Request =
                    chain.request().newBuilder().addHeader("AccessToken", token)
                        .addHeader("Content-Type", "application/json").build()
                chain.proceed(request)
            })
            .addNetworkInterceptor(interceptor).connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.SECONDS)).retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofitService(client: OkHttpClient): ApiServices {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }
}