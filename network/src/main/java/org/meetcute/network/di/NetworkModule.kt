package org.meetcute.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.meetcute.network.BuildConfig
import org.meetcute.network.data.api.auth.AuthService
import org.meetcute.network.data.api.broadcast.BroadcastService
import org.meetcute.network.data.api.chat.ChatService
import org.meetcute.network.data.api.gifts.GiftsService
import org.meetcute.network.data.api.stroy.StoryService
import org.meetcute.network.data.api.support.SupportService
import org.meetcute.network.data.api.video.VideoService
import org.meetcute.network.data.api.wallet.WalletService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor { chain ->
            val request = chain.request()
            // Add headers or intercept requests if needed
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAuthService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    fun provideBroadcastService(): BroadcastService {
        return retrofit.create(BroadcastService::class.java)
    }

    @Provides
    fun provideSupportService(): SupportService {
        return retrofit.create(SupportService::class.java)
    }

    @Provides
    fun provideGiftsService(): GiftsService {
        return retrofit.create(GiftsService::class.java)
    }

    @Provides
    fun provideWalletService(): WalletService {
        return retrofit.create(WalletService::class.java)
    }

    @Provides
    fun provideChatService(): ChatService {
        return retrofit.create(ChatService::class.java)
    }

    @Provides
    fun provideStoryService(): StoryService {
        return retrofit.create(StoryService::class.java)
    }
    @Provides
    fun provideVideoService(): VideoService {
        return retrofit.create(VideoService::class.java)
    }
}
