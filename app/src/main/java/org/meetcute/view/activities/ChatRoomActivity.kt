package org.meetcute.view.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import org.json.JSONObject
import org.meetcute.R
import org.meetcute.databinding.ActivityChatRoomBinding
import org.meetcute.databinding.ChatBelowItemBinding
import org.meetcute.databinding.ChatSceenItemBinding
import org.meetcute.databinding.ItemChatReceivedBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.loadCircleCrop
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.appUtils.uploadFile.UploadUtility
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.ChatListItem
import org.meetcute.network.data.model.ChatListResponse
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.ChatSocketResponse
import org.meetcute.view.apaters.BroadcasterModel
import org.meetcute.view.apaters.NewChatAdapter
import org.meetcute.view.fragments.bottomSheets.ChatOptionsBottomSheet
import org.meetcute.view.socket.SocketCallBacks
import org.meetcute.view.socket.SocketConnection
import org.meetcute.view.socket.SocketConstants.BROADCASTER_ID
import org.meetcute.view.socket.SocketConstants.EVENT_ROOM_JOIN
import org.meetcute.view.socket.SocketConstants.EVENT_SEND_MESSAGE
import org.meetcute.view.socket.SocketConstants.IS_TYPING
import org.meetcute.view.socket.SocketConstants.MESSAGE
import org.meetcute.view.socket.SocketConstants.ROOM_ID
import org.meetcute.view.socket.SocketConstants.ROOM_JOIN_STATUS
import org.meetcute.view.socket.SocketConstants.SEND_BY
import org.meetcute.view.socket.SocketConstants.VIEWER
import org.meetcute.viewModel.ChatViewModel
import org.meetcute.viewModel.SupportViewModel
import kotlin.math.log

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>(), SocketCallBacks {
    private val viewModel: ChatViewModel by viewModels()
    private val supportViewModel: SupportViewModel by viewModels()
    private var blockId: String? = ""
    private var from: String? = ""
    private var list: ChatListItem? = null
    private var mSocket: Socket? = null
    var isRoomJoin = true
    private var roomId: String? = ""
    private var sendimg: String? = ""
    private var broadCastId: String? = ""
    private var viewerId: String? = ""
    private var chatList = ArrayList<ChatMessageListRes.ChatData>()
    private val chatAdapter: NewChatAdapter by lazy {
        NewChatAdapter(this, pref.user?._id)
    }
    private val chatSuggestionAdapter: ChatSuggestionAdapter by lazy {
        ChatSuggestionAdapter()
    }

    override fun getLayout(): Int {
        return R.layout.activity_chat_room
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blockId = intent.getStringExtra("blockId") ?: ""
        println("======================blo$blockId")
        from = intent.getStringExtra("from") ?: ""
        if (from == "chat") {
            list = intent.getSerializableExtra("list") as ChatListItem?
            if (list?.viewer?.online != true)
                binding.ivOnline.visibility = View.INVISIBLE
            else binding.ivOnline.visibility = View.VISIBLE
           /* binding.ivOnline.visibility =
                if (list?.active == true) View.VISIBLE else View.GONE*/
            binding.ivProfile.loadCircleCrop(list?.viewer?.profileImage)
            binding.tvName.text = list?.viewer?.name ?: ""
            viewModel.joinUser(list?.viewerId ?: "", list?.broadcasterId ?: "")


        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivThreeDot.setOnClickListener {
            ChatOptionsBottomSheet(blockId ?: "", roomId ?: "").show(supportFragmentManager, "")
        }
        binding.rvHistory.adapter = chatAdapter
        binding.rvChatSuggestions.adapter = chatSuggestionAdapter
        chatSuggestionAdapter.set(suggest)
        viewModel.joinUserResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            roomId = it.value?.data?._id ?: ""
                            println("=====================romm$roomId")
                            connectSocket()
                            supportViewModel.seenMessage(roomId ?: "")

                        } else it.value?.message?.show(binding)
                    }

                    is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                    else -> {}
                }
        }
        viewModel.chatMessageListResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {
                        Loaders.show(this)
                    }

                    is NetworkResponse.Success -> {
                        Loaders.hide()
                        if (it.value?.success == true) {
                            it.value?.data?.forEach { item ->
                                chatAdapter.addItem(item)
                            }
                            broadCastId = it.value?.data?.get(0)?.broadcaster?._id ?: ""
                            viewerId = it.value?.data?.get(0)?.viewer?._id ?: ""
                        } else {
                            Loaders.hide()
                            it.value?.message?.show(binding)
                        }

                    }

                    is NetworkResponse.Failure -> {
                        Loaders.hide()
                        it.throwable?.message.show(binding)
                    }

                    else -> {}
                }
        }
        binding.ivSend.setOnClickListener {
            val typedMessage = binding.ivComment.text.toString()
            if (typedMessage.isEmpty()) {
                "Please Type message".show(binding)
            } else {
                sendMessage(typedMessage)
                binding.ivComment.setText("")
            }
        }
        binding.ivGallery.setOnClickListener {
            checkPermissionOrOpenGallery()
        }

        supportViewModel.blockedViewerResponse.observe(this, Observer {
            /*if (lifecycle.currentState == Lifecycle.State.RESUMED)*/
            when (it) {
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    if (it.value?.success == true) {
                        it.value?.message?.show(binding)
                        finish()
                    } else {
                        it.value?.message?.show(binding)
                    }
                }

                is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                else -> {}
            }
        })
        supportViewModel.reportViewerResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            it.value?.message?.show(binding)
                        } else {
                            it.value?.message?.show(binding)
                        }
                    }

                    is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                    else -> {}
                }
        }
        supportViewModel.deleteChatHistoryResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                            it.value?.message?.show(binding)
                            finish()
                        } else {
                            it.value?.message?.show(binding)
                        }
                    }

                    is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                    else -> {}
                }
        }
        supportViewModel.seenMessageResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.Success -> {
                        if (it.value?.success == true) {
                        } else {
                            it.value?.message?.show(binding)
                        }
                    }

                    is NetworkResponse.Failure -> it.throwable?.message.show(binding)
                    else -> {}
                }
        }
    }

    private fun sendMessage(message: String) {
        try {
            val messageObject = JSONObject().apply {
                put("mtype", "text")
                put(MESSAGE, message)
            }
            val `object` = JSONObject()
            `object`.put(ROOM_ID, roomId)
            `object`.put(SEND_BY, "Broadcaster")
            `object`.put(BROADCASTER_ID, broadCastId)
            `object`.put(VIEWER, viewerId)
            `object`.put(MESSAGE, messageObject)
            mSocket?.emit(EVENT_SEND_MESSAGE, `object`)
            println("===========obbb$`object`")
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
    data class Suggest(val name: String)

    private val suggest = mutableListOf<Suggest>().apply {
        add(Suggest("I love your pic❤"))
        add(Suggest("Wow❤"))
        add(Suggest("Awesome❤"))
        add(Suggest("I like it❤"))
        add(Suggest("Great pic❤"))
    }

    inner private class ChatSuggestionAdapter : BaseAdapter<Suggest>() {


        inner class ChatViewHolder(val binding: ChatBelowItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.tvChat.setOnClickListener {
                    try {
                        val messageObject = JSONObject().apply {
                            put("mtype", "text")
                            put(MESSAGE, get(absoluteAdapterPosition)?.name)
                        }
                        val `object` = JSONObject()
                        `object`.put(ROOM_ID, roomId)
                        `object`.put(SEND_BY, "Broadcaster")
                        `object`.put(BROADCASTER_ID, broadCastId)
                        `object`.put(VIEWER, viewerId)
                        `object`.put(MESSAGE, messageObject)
                        mSocket?.emit(EVENT_SEND_MESSAGE, `object`)
                        println("===========obbbADDD$`object`")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ChatViewHolder(
                ChatBelowItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ChatViewHolder).apply {
                binding.tvChat.text = get(absoluteAdapterPosition)?.name
            }
        }
    }

    /* private class ChatAdapter : BaseAdapter<Int>() {

         companion object {
             private val TYPE_SENDER = 12
             private val TYPE_RECEIVER = 13
         }

         override fun getItemViewType(position: Int): Int {
             return if (position % 2 == 0) TYPE_SENDER else TYPE_RECEIVER
         }


         inner class SenderViewHolder(val binding: ChatSceenItemBinding) :
             RecyclerView.ViewHolder(binding.root) {
             init {

             }
         }

         inner class ReceiverViewHolder(val binding: ItemChatReceivedBinding) :
             RecyclerView.ViewHolder(binding.root) {
             init {

             }
         }


         override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
             return when (viewType) {
                 TYPE_SENDER -> SenderViewHolder(
                     ChatSceenItemBinding.inflate(
                         LayoutInflater.from(parent.context),
                         parent,
                         false
                     )
                 )

                 TYPE_RECEIVER -> ReceiverViewHolder(
                     ItemChatReceivedBinding.inflate(
                         LayoutInflater.from(parent.context),
                         parent,
                         false
                     )
                 )

                 else -> SenderViewHolder(
                     ChatSceenItemBinding.inflate(
                         LayoutInflater.from(parent.context),
                         parent,
                         false
                     )
                 )
             }

         }

         override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
             when (holder.itemViewType) {
                 TYPE_SENDER -> {
                     (holder as SenderViewHolder).apply {
                         if (position == 0) {
                             binding.llToday.visibility = View.VISIBLE
                         } else {
                             binding.llToday.visibility = View.GONE

                         }
                     }
                 }

                 TYPE_RECEIVER -> {
                     (holder as ReceiverViewHolder).apply {
                         binding.llToday.visibility = View.GONE
                     }
                 }
             }

         }
     }*/

    override fun onConnect(vararg args: Any?) {
        Log.d("TAG", "onConnect:  .......")
        isRoomJoin = true
        roomJoin()
    }

    override fun onDisconnect(vararg args: Any?) {
        Log.d("TAG", "onDisconnect: ..........")
    }

    override fun onRoomLeave(vararg args: Any?) {
        Log.d("TAG", "onRoomLeave: ........." + args[0].toString())
    }

    override fun onConnectError(vararg args: Any?) {

    }

    override fun onRoomJoin(vararg args: Any?) {
        Log.d("TAG", "onRoomJoin: ..............")
        isRoomJoin = false
    }

    override fun onMessage(vararg args: Any?) {
        Log.e("TAG", "onMessage: .........${Gson().toJson(args[0])}")
        runOnUiThread {
            val rr = Gson().toJson(args[0]).replace("[", "").replace("]", "")
            try {
                val data = JSONObject(rr)
                val temp: ChatSocketResponse =
                    Gson().fromJson(data.toString(), ChatSocketResponse::class.java)
                val remoteMessage = ChatMessageListRes.ChatData(
                    temp.nameValuePairs?.__v ?: 0,
                    temp.nameValuePairs?._id ?: "",
                    ChatMessageListRes.ChatData.Broadcaster(
                        temp.nameValuePairs?.broadcaster?.nameValuePairs?._id ?: "",
                        temp.nameValuePairs?.broadcaster?.nameValuePairs?.name ?: "",
                        temp.nameValuePairs?.broadcaster?.nameValuePairs?.profileImage ?: ""
                    ),
                    temp.nameValuePairs?.createdAt ?: "",
                    temp.nameValuePairs?.isBroadcasterDelete,
                    temp.nameValuePairs?.isViewerDelete,
                    ChatMessageListRes.ChatData.Message(
                        temp.nameValuePairs?.message?.nameValuePairs?.message ?: "",
                        temp.nameValuePairs?.message?.nameValuePairs?.mtype ?: ""
                    ),
                    temp.nameValuePairs?.read,
                    temp.nameValuePairs?.roomId ?: "",
                    temp.nameValuePairs?.sentBy ?: "",
                    temp.nameValuePairs?.updatedAt ?: "",
                    ChatMessageListRes.ChatData.Viewer(
                        temp.nameValuePairs?.viewer?.nameValuePairs?._id ?: "",
                        temp.nameValuePairs?.viewer?.nameValuePairs?.name ?: "",
                        temp.nameValuePairs?.viewer?.nameValuePairs?.online,
                        temp.nameValuePairs?.viewer?.nameValuePairs?.profileImage ?: ""
                    )
                )
                chatAdapter.addItem(remoteMessage)
                binding.rvHistory.scrollToPosition(chatAdapter.itemCount - 1)
            } catch (e: Exception) {
                Log.d("TAG", " exception on message " + e.message)
                e.printStackTrace()
            }

        }
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
        Log.e("TAG", "onIsTyping: .........${Gson().toJson(args[0])}")
        runOnUiThread {
            if (!isStillTyping) {
                //chatAdapter.addTyping()
                binding.chat.visibility = View.VISIBLE
            }
            startTimer()
            isStillTyping = true
        }

    }

    override fun onAnswerCall(vararg args: Any?) {

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
    }
    override fun onRoomJoinStatus(vararg args: Any?) {
        Log.e("TAG", "onRoomJoinStatus: .........${Gson().toJson(args[0])}")
    }
    private var isStillTyping = false

    private var timer: CountDownTimer? = null
    fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                // chatAdapter.removeTyping()
                binding.chat.visibility = View.INVISIBLE
                isStillTyping = false
                Log.d("TAG", "onFinish:........")
            }
        }.start()
    }

    fun roomJoin() {
        try {
            if (isRoomJoin) {
                val `object` = JSONObject()
                `object`.put(ROOM_ID, roomId)
                mSocket?.emit(EVENT_ROOM_JOIN, `object`)
                viewModel.chatMessageList(roomId ?: "")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
        roomId = ""
    }

    private fun checkPermissionOrOpenGallery() {
        if (ifPermissions()) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryPicker.launch(galleryIntent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                )
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun ifPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show dialog for permissions
                false
            } else {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (ifPermissions()) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryPicker.launch(galleryIntent)
            }
        }

    private val galleryPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = it.data?.data
                sendimg = Utils.getFileFromUri(this, selectedImageUri ?: Uri.EMPTY).toString()
                UploadUtility(this).uploadFile(sendimg!!){
                    try {
                        val messageObject = JSONObject().apply {
                            put("mtype", "media")
                            put(MESSAGE, it)
                        }
                        val `object` = JSONObject()
                        `object`.put(ROOM_ID, roomId)
                        `object`.put(SEND_BY, "Broadcaster")
                        `object`.put(BROADCASTER_ID, broadCastId)
                        `object`.put(VIEWER, viewerId)
                        `object`.put(MESSAGE, messageObject)
                        mSocket?.emit(EVENT_SEND_MESSAGE, `object`)
                        println("===========obbbuuuu$`object`")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }
    override fun onShowKeyboard(keyboardHeight: Int) {
        val `object` = JSONObject()
        `object`.put(ROOM_ID, roomId)
        mSocket?.emit(IS_TYPING, `object`)
        println("==================chat${`object`}")
    }

    override fun onHideKeyboard() {
        chatAdapter.removeTyping()
    }
}