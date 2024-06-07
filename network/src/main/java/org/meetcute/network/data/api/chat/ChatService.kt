package org.meetcute.network.data.api.chat

import org.meetcute.network.data.model.ChatListResponse
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.JoinUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ChatService {

    @GET("broadcaster/chatHistory")
    suspend fun getChatList(@Header("Authorization") jwtToken:String): Response<ChatListResponse>

    @GET("getChatRoom")
    suspend fun joinUser(
        @Header("Authorization") jwtToken: String,
        @Query("viewerId") viewerId: String,
        @Query("broadcasterId") broadcasterId: String
    ): Response<JoinUserResponse>

    @GET("broadcaster/chatMessageList")
    suspend fun chatMessageList(
        @Header("Authorization") jwtToken: String,
        @Query("roomId") roomId: String
    ): Response<ChatMessageListRes>

}