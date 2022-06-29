package land.kho.meta.di

import android.content.Context
import land.kho.meta.data.api.ApiConstants
import land.kho.meta.data.api.ApiList
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.repository.RepositoryImpl
import land.kho.meta.domain.usecases.PreferencesUseCase
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("authToken")
    fun provideToken(
        preferencesUseCase: PreferencesUseCase
    ): String {
        return runBlocking {
            preferencesUseCase.getToken().first() ?: ""
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp3Interceptor(
        @Named("authToken") authToken: String
    ): Interceptor {
        return runBlocking {
            Interceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $authToken")
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttp3Client(
        @ApplicationContext context: Context,
        requestMiddleware: RequestMiddleware,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(requestMiddleware)
        .addInterceptor(ChuckerInterceptor.Builder(context).build())
        .build()

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun getApi(retrofit: Retrofit): ApiList = retrofit.create(ApiList::class.java)

    @Provides
    fun getRepository(api: ApiList): Repository = RepositoryImpl(api)

}