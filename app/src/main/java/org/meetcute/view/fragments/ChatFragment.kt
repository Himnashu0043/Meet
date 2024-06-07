package org.meetcute.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import org.json.JSONObject
import org.meetcute.R
import org.meetcute.databinding.CardAllConversationsBinding
import org.meetcute.databinding.FragmentChatBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.loadCircleCrop
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.ChatListItem
import org.meetcute.view.activities.ChatRoomActivity
import org.meetcute.view.fragments.bottomSheets.FilterBottomSheet
import org.meetcute.view.socket.SocketCallBacks
import org.meetcute.view.socket.SocketConnection
import org.meetcute.view.socket.SocketConstants
import org.meetcute.viewModel.ChatViewModel

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(), SocketCallBacks {
    private var mSocket: Socket? = null
    private val chatViewModel: ChatViewModel by viewModels()

    data class ChatRooms(val icon: Int, val name: String, val message: String)

    private val chatListAdapter: ChatListAdapter by lazy {
        ChatListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectSocket()
        Handler(Looper.getMainLooper()).postDelayed({
            chatViewModel.getChatList()
        }, 1500)
        chatViewModel.chatListResponse.observe(viewLifecycleOwner) {

            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(requireActivity())
                }
                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        chatListAdapter.set(it.value?.data ?: emptyList())
                    }
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message.show(binding)
                }

                else -> {}
            }
        }

        binding.ivFilters.setOnClickListener {
            FilterBottomSheet().show(requireActivity().supportFragmentManager, "")
        }
        binding.rvAllConversations.adapter = chatListAdapter

    }

    private fun connectSocket() {
        try {
            mSocket = SocketConnection.connectSocket(this)
            if (mSocket?.connected() != true) {
                mSocket?.connected()
            }


        } catch (e: Exception) {
            Log.e("TAG", "connectSocket: ${e.printStackTrace()}")
            e.printStackTrace()
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_chat
    }

    inner class ChatListAdapter : BaseAdapter<ChatListItem>() {

        inner class ChatListViewHolder(val binding: CardAllConversationsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    startActivity(
                        Intent(
                            requireActivity(),
                            ChatRoomActivity::class.java
                        ).putExtra("blockId", get(absoluteAdapterPosition)?.viewer?._id ?: "")
                            .putExtra("from", "chat").putExtra("list", get(absoluteAdapterPosition))
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ChatListViewHolder(
                CardAllConversationsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ChatListViewHolder).apply {
                /*if (position % 3 == 0) {
                    binding.flUnreadCount.visibility = View.VISIBLE
                } else {
                    binding.flUnreadCount.visibility = View.GONE
                }*/
                if (get(absoluteAdapterPosition)?.broadcaster_count != 0)
                    binding.tvCount.text = "${get(absoluteAdapterPosition)?.broadcaster_count ?: 0}"
                else binding.flUnreadCount.visibility = View.INVISIBLE

                binding.ivStatus.visibility =
                    if (get(position)?.active == true) View.VISIBLE else View.GONE
                binding.ivprofilentification.loadCircleCrop(get(position)?.viewer?.profileImage)
                binding.tvFirstName.text = get(position)?.viewer?.name
                if (get(absoluteAdapterPosition)?.lastMessage?.mtype == "media") {
                    binding.tvVideCall.text = "photo"
                } else {
                    binding.tvVideCall.text = get(position)?.lastMessage?.message
                }
                binding.tvMin.text = Utils.timeAgo(get(absoluteAdapterPosition)?.updatedAt ?: "")

            }
        }
    }

    override fun onConnect(vararg args: Any?) {
        Log.e("TAG", "onConnecthiii: .........${Gson().toJson(args[0])}")
        try {
            val `object` = JSONObject()
            `object`.put(SocketConstants.BROADCASTER1, pref.broadcastId)
            mSocket?.emit(SocketConstants.JOIN_USER_ROOM, `object`)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDisconnect(vararg args: Any?) {

    }

    override fun onRoomLeave(vararg args: Any?) {

    }

    override fun onConnectError(vararg args: Any?) {

    }

    override fun onRoomJoin(vararg args: Any?) {
        Log.e("TAG", "onRoomJoinhiii: .........${Gson().toJson(args[0])}")
    }

    override fun onMessage(vararg args: Any?) {

    }

    override fun onLiveCount(vararg args: Any?) {

    }

    override fun onBroadcastJoin(vararg args: Any?) {

    }

    override fun onContestStart(vararg args: Any?) {

    }

    override fun onContestUpdatedRanking(vararg args: Any?) {

    }

    override fun onTagGift(vararg args: Any?) {

    }

    override fun onVideoRequest(vararg args: Any?) {

    }

    override fun onReceivedBroadcastGift(vararg args: Any?) {

    }

    override fun onIsTyping(vararg args: Any?) {

    }

    override fun onAnswerCall(vararg args: Any?) {
        Log.e("TAG", "onAnswerCall: .........${Gson().toJson(args[0])}")
        /* binding.remoteVideoViewContainer.visibility = View.VISIBLE
         test.run {
             setupRemoteVideo(uid)
         }*/

    }

    override fun onEndCall(vararg args: Any?) {
        Log.e("TAG", "onEndCall: .........${Gson().toJson(args[0])}")
    }

    override fun onReceivedVideoGift(vararg args: Any?) {
        Log.e("TAG", "onReceivedVideoGift: .........${Gson().toJson(args[0])}")
    }

    override fun onPremiumBroadcastMinute(vararg args: Any?) {
        Log.e("TAG", "onPremiumBroadcastMinute: .........${Gson().toJson(args[0])}")
    }

    override fun onVideoCallMinute(vararg args: Any?) {
        Log.e("TAG", "onVideoCallMinute: .........${Gson().toJson(args[0])}")
    }

    override fun onGetNewMessage(vararg args: Any?) {
        Log.e("TAG", "onGetNewMessage: .........${Gson().toJson(args[0])}")
        chatViewModel.getChatList()
    }

    override fun onRoomJoinStatus(vararg args: Any?) {
        Log.e("TAG", "onRoomJoinStatus: .........${Gson().toJson(args[0])}")
    }

    override fun onResume() {
        super.onResume()
        chatViewModel.getChatList()

    }
}