package org.meetcute.network.data.api.chat

import org.meetcute.network.data.model.ChatListResponse
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.JoinUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface Chat {

    suspend fun getChatList(jwtToken:String): Response<ChatListResponse>
    suspend fun joinUser(jwtToken:String,viewerId: String,broadcasterId: String): Response<JoinUserResponse>
    suspend fun chatMessageList(jwtToken: String, roomId: String): Response<ChatMessageListRes>
}