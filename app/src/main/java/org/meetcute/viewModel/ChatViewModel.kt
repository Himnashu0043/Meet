package org.meetcute.viewModel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.api.chat.Chat
import org.meetcute.network.data.model.ChatListResponse
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.JoinUserResponse
import org.meetcute.network.di.ChatImpl1
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(@ChatImpl1 private val chat: Chat) : BaseViewModel() {

    val chatListResponse = MutableLiveData<NetworkResponse<ChatListResponse>?>()
    fun getChatList() {
        io {
            chatListResponse.value = NetworkResponse.Loading
            chatListResponse.value = api { chat.getChatList(pref.user?.jwtToken ?: "") }
        }
    }

    val joinUserResponse = MutableLiveData<NetworkResponse<JoinUserResponse>?>()
    fun joinUser(viewerId: String,broadcasterId: String) {
        io {
            joinUserResponse.value = NetworkResponse.Loading
            joinUserResponse.value = api { chat.joinUser(pref.user?.jwtToken ?: "",viewerId,broadcasterId) }
        }
    }

    val chatMessageListResponse = MutableLiveData<NetworkResponse<ChatMessageListRes>?>()
    fun chatMessageList(
        roomId: String
    ) {
        io {
            chatMessageListResponse.value = NetworkResponse.Loading
            chatMessageListResponse.value =
                api { chat.chatMessageList(pref.user?.jwtToken ?: "", roomId) }
        }
    }
}