package org.meetcute.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.meetcute.network.data.api.auth.Auth
import org.meetcute.network.data.api.auth.AuthImpl
import org.meetcute.network.data.api.auth.AuthService
import org.meetcute.network.data.api.broadcast.Broadcast
import org.meetcute.network.data.api.broadcast.BroadcastImpl
import org.meetcute.network.data.api.broadcast.BroadcastService
import org.meetcute.network.data.api.chat.Chat
import org.meetcute.network.data.api.chat.ChatService
import org.meetcute.network.data.api.gifts.Gifts
import org.meetcute.network.data.api.gifts.GiftsImpl
import org.meetcute.network.data.api.gifts.GiftsService
import org.meetcute.network.data.api.stroy.Story
import org.meetcute.network.data.api.stroy.StoryImpl
import org.meetcute.network.data.api.stroy.StoryService
import org.meetcute.network.data.api.support.Support
import org.meetcute.network.data.api.support.SupportImpl
import org.meetcute.network.data.api.support.SupportService
import org.meetcute.network.data.api.video.Video
import org.meetcute.network.data.api.video.VideoImpl
import org.meetcute.network.data.api.video.VideoService
import org.meetcute.network.data.api.wallet.Wallet
import org.meetcute.network.data.api.wallet.ChatImpl
import org.meetcute.network.data.api.wallet.WalletImpl
import org.meetcute.network.data.api.wallet.WalletService

@Module
@InstallIn(SingletonComponent::class)
class ImplModule {

    @AuthImpl1
    @Provides
    fun provideAuth(authService: AuthService): Auth {
        return AuthImpl(authService)
    }

    @BroadcastImpl1
    @Provides
    fun provideBroadcast(authService: BroadcastService): Broadcast {
        return BroadcastImpl(authService)
    }

    @GiftsImpl1
    @Provides
    fun provideGifts(authService: GiftsService): Gifts {
        return GiftsImpl(authService)
    }

    @SupportImpl1
    @Provides
    fun provideSupport(authService: SupportService): Support {
        return SupportImpl(authService)
    }

    @WalletImpl1
    @Provides
    fun provideWallet(authService: WalletService): Wallet {
        return WalletImpl(authService)
    }

    @ChatImpl1
    @Provides
    fun provideChat(authService: ChatService): Chat {
        return ChatImpl(authService)
    }

    @StoryImpl1
    @Provides
    fun provideStoryService(authService: StoryService): Story {
        return StoryImpl(authService)
    }

    @VideoImpl1
    @Provides
    fun provideVideoService(authService: VideoService): Video {
        return VideoImpl(authService)
    }


}