package org.meetcute.network.data.api.wallet

import org.meetcute.network.data.api.chat.Chat
import org.meetcute.network.data.api.chat.ChatService
import org.meetcute.network.data.model.ChatListResponse
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.JoinUserResponse
import retrofit2.Response


class ChatImpl(private val chat: ChatService) : Chat {

    override suspend fun getChatList(jwtToken: String): Response<ChatListResponse> {
        return chat.getChatList(jwtToken)
    }

    override suspend fun joinUser(jwtToken: String,viewerId: String,broadcasterId: String): Response<JoinUserResponse> {
        return chat.joinUser(jwtToken,viewerId,broadcasterId)
    }
    override suspend fun chatMessageList(
        jwtToken: String,
        roomId: String
    ): Response<ChatMessageListRes> {
        return chat.chatMessageList(jwtToken, roomId)
    }
}