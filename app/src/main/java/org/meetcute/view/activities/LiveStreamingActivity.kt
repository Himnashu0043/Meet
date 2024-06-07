package org.meetcute.view.activities

import android.Manifest
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtm.MessageEvent
import io.socket.client.Socket
import org.json.JSONObject
import org.meetcute.R
import org.meetcute.databinding.ActivityLiveStreamingBinding
import org.meetcute.databinding.ItemGiftsBinding
import org.meetcute.databinding.ItemLiveCommentsBinding
import org.meetcute.appUtils.BaseAdapter
import org.meetcute.appUtils.Loaders
import org.meetcute.appUtils.common.Utils.load
import org.meetcute.appUtils.common.Utils.show
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.AgoraSendData
import org.meetcute.network.data.model.ChatMessageListRes
import org.meetcute.network.data.model.ChatSocketResponse
import org.meetcute.network.data.model.ContestUpdateRankingResponse
import org.meetcute.network.data.model.GetAllGiftsData
import org.meetcute.network.data.model.ReceivedGiftResponse
import org.meetcute.network.data.model.ViewSocketResponse
import org.meetcute.view.fragments.bottomSheets.CommentsBottomSheet
import org.meetcute.view.fragments.bottomSheets.EndBroadcastPremiumBottomSheet
import org.meetcute.view.fragments.bottomSheets.GiftersViewerBottomSheet
import org.meetcute.view.fragments.bottomSheets.GoPremiumBottomSheet
import org.meetcute.view.fragments.bottomSheets.TagGiftBottomSheet
import org.meetcute.view.fragments.bottomSheets.VideoCallRequestBottomSheet
import org.meetcute.view.socket.SocketCallBacks
import org.meetcute.view.socket.SocketConnection
import org.meetcute.view.socket.SocketConstants
import org.meetcute.viewModel.GiftsViewModel
import org.meetcute.viewModel.SupportViewModel
import java.nio.charset.Charset


@AndroidEntryPoint
class LiveStreamingActivity : BaseLiveStreamingActivity<ActivityLiveStreamingBinding>(),
    SurfaceHolder.Callback,
    View.OnClickListener, SocketCallBacks {
    private lateinit var audioManager: AudioManager
    private val viewModel: SupportViewModel by viewModels()
    private val giftsViewModel: GiftsViewModel by viewModels()
    private lateinit var surfaceHolder: SurfaceHolder
    private var camera: Camera? = null
    private var CAMERA_PERMISSION_REQUEST = 111
    private var mSocket: Socket? = null
    var isRoomJoin = true
    var count = 0
    var getPremiumCoin: Int = 0
    private val giftAdapter: GiftsAdapter by lazy {
        GiftsAdapter()
    }

    private val commentsAdapter: LiveCommentsAdapter by lazy {
        LiveCommentsAdapter()
    }

    override val localUid: Int
        get() = R.id.surfaceView
    override val localVideo: SurfaceView
        get() = binding.surfaceView



    override fun getLayout(): Int {
        return R.layout.activity_live_streaming
    }

    fun goPremium() {
        binding.llGroup.visibility = View.GONE
        binding.llGift.visibility = View.GONE
        binding.ivLivePrime.visibility = View.VISIBLE
        binding.ivLive.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("================token${pref.getRTMToken}")
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        binding.tvCoins.text =
            "${pref.giftCoin}"
        surfaceHolder = binding.surfaceView.holder
        surfaceHolder.addCallback(this)
        connectSocket()
        binding.llCoins.setOnClickListener {
            GiftersViewerBottomSheet().show(supportFragmentManager, "")
        }

        binding.ivContest.setOnClickListener {
            binding.tvStartContest.performClick()

        }
        binding.tvStartContest.setOnClickListener {
            if (binding.includeCoinGiven.isVisible) {
                binding.ivContest.isSelected = false
                binding.tvStartContest.text = "Start Contest"
                binding.includeCoinGiven.visibility = View.GONE
                try {
                    val `object` = JSONObject()
                    `object`.put(
                        SocketConstants.BROADCASTER,
                        broadcastViewModel.currentBrId
                    )
                    mSocket?.emit(SocketConstants.STOP_BROADCAST_CONTEST, `object`)
                    println("========================Stop$`object`")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                binding.ivContest.isSelected = true

                binding.tvStartContest.text = "Stop Contest"
                try {
                    val `object` = JSONObject()
                    `object`.put(SocketConstants.BROADCASTER, pref.broadcastId)
                    mSocket?.emit(SocketConstants.START_BROADCAST_CONTEST, `object`)
                    println("======================const$`object`")
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }
        binding.cross.setOnClickListener {
            if (binding.llGift.isVisible)
                EndBroadcastPremiumBottomSheet(isPremium = true, premiumCoin = getPremiumCoin).show(
                    supportFragmentManager,
                    ""
                )
            if (binding.rlGoPremium.isVisible)
                EndBroadcastPremiumBottomSheet(
                    isPremium = false,
                    premiumCoin = getPremiumCoin
                ).show(supportFragmentManager, "")
            else EndBroadcastPremiumBottomSheet(
                isPremium = true,
                premiumCoin = getPremiumCoin
            ).show(supportFragmentManager, "")
        }
        binding.rlChats.setOnClickListener {
            enterPIPMode()
            //CommentsBottomSheet().show(supportFragmentManager, "")
        }
        binding.rlVideoRequest.setOnClickListener {
            VideoCallRequestBottomSheet().show(supportFragmentManager, "")
        }
        binding.ivMinimize.setOnClickListener {
            enterPIPMode()
        }

        binding.rlGoPremium.setOnClickListener {
            GoPremiumBottomSheet(positiveButton = ::goPremium).show(supportFragmentManager, "")
        }

        binding.rvComments.adapter = commentsAdapter
        binding.rvGifts.adapter = giftAdapter


        giftsViewModel.allGiftsResponse.observe(this) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED)
                when (it) {
                    is NetworkResponse.Success -> {
                        if (!it.value?.data.isNullOrEmpty()) {
                            giftAdapter.set(it.value?.data ?: emptyList())
                        } else it.value?.message.show(binding)
                    }

                    is NetworkResponse.Failure -> it.throwable?.message?.show(binding)
                    else -> {}
                }
        }
        giftsViewModel.getAllGifts()
        viewModel.setPremiumResponse.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> {
                    Loaders.show(this)
                }

                is NetworkResponse.Success -> {
                    Loaders.hide()
                    if (it.value?.success == true) {
                        try {
                            val `object` = JSONObject()
                            `object`.put(
                                SocketConstants.BROADCASTER,
                                broadcastViewModel.currentBrId
                            )
                            mSocket?.emit(SocketConstants.STOP_BROADCAST_CONTEST, `object`)
                            println("========================Stop$`object`")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else it.value?.message.show(binding)
                }

                is NetworkResponse.Failure -> {
                    Loaders.hide()
                    it.throwable?.message?.show(binding)
                }

                else -> {}
            }
        }
        broadcastViewModel.tagGiftAction.observe(this) {
            if (it != null) {
                count = 1
                // TODO emit socket for tag gifts
                try {
                    val `object` = JSONObject()
                    `object`.put(SocketConstants.BROADCASTER, it.broadcastId)
                    `object`.put(SocketConstants.GIFTID, it.giftId)
                    `object`.put(SocketConstants.MILE_STONE, it.coin)
                    `object`.put(SocketConstants.MESSAGE, it.message)
                    mSocket?.emit(SocketConstants.TAG_GIFT, `object`)
                    println("===========obbbTag$`object`")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        binding.ivSound.setOnClickListener {
            binding.ivUnSound.visibility = View.VISIBLE
            binding.ivSound.visibility = View.INVISIBLE
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)

        }
        binding.ivUnSound.setOnClickListener {
            binding.ivUnSound.visibility = View.INVISIBLE
            binding.ivSound.visibility = View.VISIBLE
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)

        }
        binding.ivSend.setOnClickListener {
            val messageObject = JSONObject().apply {
                put("ProfileImgUrl", pref.user?.profileImage)
                put("message", binding.etMessage.text.trim().toString())
                put("sender", pref.user?.name)
            }
            val messageBytes = messageObject.toString()
            commentsAdapter.add(
                AgoraSendData(
                    pref.user?.profileImage ?: "",
                    binding.etMessage.text.trim().toString(),
                    pref.user?.name ?: ""
                )
            )
            sendComment(messageBytes)
            binding.etMessage.setText("")
        }

    }

    override fun onShowKeyboard(keyboardHeight: Int) {
        binding.rvGifts.visibility = View.GONE
        binding.rvComments.visibility = View.GONE
    }

    override fun onHideKeyboard() {
        binding.rvComments.visibility = View.VISIBLE
        binding.rvGifts.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        enterPIPMode()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIPMode()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
        if (isInPictureInPictureMode) {
            binding.clContent.visibility = View.GONE
        } else {
            binding.clContent.visibility = View.VISIBLE
        }
    }

    private fun enterPIPMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(12, 16) // Aspect ratio for PIP mode
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }


    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = h.toDouble() / w
        if (sizes == null) return null
        var optimalSize: Camera.Size? = null
        var minDiff = Double.MAX_VALUE
        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(holder)
            val mSupportedPreviewSizes = camera?.parameters?.supportedPreviewSizes
            if (mSupportedPreviewSizes != null) {
                val mPreviewSize = getOptimalPreviewSize(
                    mSupportedPreviewSizes,
                    binding.surfaceView.width,
                    binding.surfaceView.height
                )
                val parameters: Camera.Parameters? = camera?.parameters
                parameters?.setPreviewSize(mPreviewSize!!.width, mPreviewSize.height)
                camera?.parameters = parameters
                camera?.startPreview()
            } else {
                camera?.startPreview()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                surfaceHolder.addCallback(this)
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun enabledGifts() {
        binding.llGift.visibility = View.VISIBLE
        binding.ivImgGift.load(broadcastViewModel.tagGiftAction.value?.img)
        binding.tvCoinTagGift.text = "${broadcastViewModel.tagGiftAction.value?.coin ?: 0}"
        binding.ProgressBar.progress = broadcastViewModel.tagGiftAction.value?.coin ?: 0
//        binding.includeCoinGiven.visibility = View.VISIBLE
    }


    inner class GiftsAdapter : BaseAdapter<GetAllGiftsData>() {

        inner class GiftViewHolder(val binding: ItemGiftsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    if (count == 1) {
                        Toast.makeText(it.context, "Not send Tag", Toast.LENGTH_SHORT).show()
                    } else {
                        broadcastViewModel.currentGift = get(absoluteAdapterPosition)
                        TagGiftBottomSheet {
                            enabledGifts()
                        }.show(supportFragmentManager, "")
                    }

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return GiftViewHolder(
                ItemGiftsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as GiftViewHolder).apply {
                binding.ivIcon.load(get(position)?.smallImageUrl)
                binding.tvCoins.text = "${get(position)?.price}"
            }
        }
    }


    private val comments = mutableListOf<Comments>().apply {
        add(Comments(R.drawable.comment_one, "James Christensen :", "liked this"))
        add(
            Comments(
                R.drawable.comment_two, "Andrew :", "Hey!!! You look so beautiful" +
                        " tonight \uD83E\uDD79❤\uFE0F❤\uFE0F"
            )
        )
        add(Comments(R.drawable.comment_three, "Stella :", "Do you have Facebook?"))
        add(Comments(R.drawable.comment_four, "James Christensen :", "liked this"))
//        add(Comments(R.drawable.comment_five, "Stella :", "shared this live stream"))
    }

    data class Comments(val icon: Int, val name: String, val message: String)

    private class LiveCommentsAdapter : BaseAdapter<AgoraSendData>() {

        inner class LiveCommentViewHolder(val binding: ItemLiveCommentsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            init {

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LiveCommentViewHolder(
                ItemLiveCommentsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as LiveCommentViewHolder).apply {
                binding.tvComment.text = get(position)?.message
                binding.tvUserName.text = get(position)?.sender
                binding.ivProfile.load(get(position)?.ProfileImgUrl ?: "")
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {

        }
    }

    override fun onConnect(vararg args: Any?) {
        Log.d("TAG", "onConnect:  .......")
        isRoomJoin = true
        roomJoin()
    }

    override fun onDisconnect(vararg args: Any?) {
        Log.d("TAG", "onDisconnect: .........")
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
    }

    override fun onLiveCount(vararg args: Any?) {
        Log.e("TAG", "onLiveCount: .........${Gson().toJson(args[0])}")
        runOnUiThread {
            val rr = Gson().toJson(args[0]).replace("[", "").replace("]", "")
            try {
                val data = JSONObject(rr)
                val temp: ViewSocketResponse =
                    Gson().fromJson(data.toString(), ViewSocketResponse::class.java)
                if (temp.nameValuePairs?.currentViews!! < 1000) {
                    binding.tvViewCount.text = "${temp.nameValuePairs?.currentViews ?: 0}"
                } else {
                    binding.tvViewCount.text = "${temp.nameValuePairs?.currentViews ?: 0} k"
                }


            } catch (e: Exception) {
                Log.d("TAG", " exception on message " + e.message)
                e.printStackTrace()
            }

        }
    }

    override fun onBroadcastJoin(vararg args: Any?) {

    }

    override fun onContestStart(vararg args: Any?) {
        Log.e("TAG", "onContestStart: .........${Gson().toJson(args[0])}")
    }

    override fun onContestUpdatedRanking(vararg args: Any?) {
        Log.e("TAG", "onContestUpdatedRanking: .........${Gson().toJson(args[0])}")
        runOnUiThread {
            val rr = Gson().toJson(args[0]).toString().trim('[').trim(']')
            try {
                binding.includeCoinGiven.visibility = View.VISIBLE
                val data = JSONObject(rr)
                val temp: ContestUpdateRankingResponse =
                    Gson().fromJson(data.toString(), ContestUpdateRankingResponse::class.java)
                val sortedList = temp.values?.sortedBy { it.nameValuePairs?.coin }
                if (sortedList?.size == 1) {
                    binding.firstDiv.visibility = View.INVISIBLE
                    binding.secondDiv.visibility = View.INVISIBLE
                    binding.thirdDeiv.visibility = View.INVISIBLE
                    binding.secondLay.visibility =
                        View.INVISIBLE
                    binding.third.visibility =
                        View.INVISIBLE
                    binding.ivConProfile.load(
                        sortedList.get(0).nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.profileImage
                            ?: ""
                    )
                    binding.tvContFirstName.text =
                        sortedList.get(0).nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.name
                            ?: ""
                    binding.tvContfirstCoin.text =
                        "${sortedList.get(0).nameValuePairs?.coin ?: 0}"

                } else if (sortedList?.size == 2) {
                    binding.firstDiv.visibility = View.VISIBLE
                    binding.thirdDeiv.visibility = View.INVISIBLE
                    binding.secondDiv.visibility = View.INVISIBLE
                    binding.secondLay.visibility =
                        View.VISIBLE
                    binding.ivConProfile.load(
                        sortedList.get(1).nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.profileImage
                            ?: ""
                    )
                    binding.tvContFirstName.text =
                        sortedList.get(1).nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.name
                            ?: ""
                    binding.tvContfirstCoin.text =
                        "${sortedList.get(1).nameValuePairs?.coin ?: 0}"



                    binding.ivProfileTwo.load(
                        sortedList.get(0).nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.profileImage
                            ?: ""
                    )
                    binding.tvContSecName.text =
                        sortedList.get(0).nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.name
                            ?: ""
                    binding.tvConstSecondCoin.text =
                        "${sortedList.get(0).nameValuePairs?.coin ?: 0}"

                } else {
                    binding.firstDiv.visibility = View.VISIBLE
                    binding.thirdDeiv.visibility = View.VISIBLE
                    binding.secondDiv.visibility = View.VISIBLE
                    binding.secondLay.visibility =
                        View.VISIBLE
                    binding.third.visibility =
                        View.VISIBLE

                    binding.ivConProfile.load(
                        sortedList?.get(2)?.nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.profileImage
                            ?: ""
                    )
                    binding.tvContFirstName.text =
                        sortedList?.get(2)?.nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.name
                            ?: ""
                    binding.tvContfirstCoin.text =
                        "${sortedList?.get(2)?.nameValuePairs?.coin ?: 0}"



                    binding.ivProfileTwo.load(
                        sortedList?.get(1)?.nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.profileImage
                            ?: ""
                    )
                    binding.tvContSecName.text =
                        sortedList?.get(1)?.nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.name
                            ?: ""
                    binding.tvConstSecondCoin.text =
                        "${sortedList?.get(1)?.nameValuePairs?.coin ?: 0}"



                    binding.ivProfile3.load(
                        sortedList?.get(0)?.nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.profileImage
                            ?: ""
                    )
                    binding.tvConstThirdName.text =
                        sortedList?.get(0)?.nameValuePairs?.viewer?.values?.get(0)?.nameValuePairs?.name
                            ?: ""
                    binding.tvCoin3.text =
                        "${sortedList?.get(0)?.nameValuePairs?.coin ?: 0}"
                }


            } catch (e: Exception) {
                Log.d("TAG", " exception on message " + e.message)
                e.printStackTrace()
            }

        }
    }

    override fun onTagGift(vararg args: Any?) {
        Log.e("TAG", "onTagGift: .........${Gson().toJson(args[0])}")
    }

    override fun onVideoRequest(vararg args: Any?) {
        Log.e("TAG", "onVideoRequest: .........${Gson().toJson(args[0])}")
        val rr = Gson().toJson(args[0]).replace("[", "").replace("]", "")
        binding.tvVideoRequestCount.text = rr
    }

    override fun onReceivedBroadcastGift(vararg args: Any?) {
        Log.e("TAG", "onReceivedBroadcastGift: .........${Gson().toJson(args[0])}")
        runOnUiThread {
            val rr = Gson().toJson(args[0]).replace("[", "").replace("]", "")
            try {
                val data = JSONObject(rr)
                val temp: ReceivedGiftResponse =
                    Gson().fromJson(data.toString(), ReceivedGiftResponse::class.java)
                println("========================coin${temp.nameValuePairs?.receivedGift?.nameValuePairs?.coin}")
                pref.giftCoin += temp.nameValuePairs?.receivedGift?.nameValuePairs?.coin ?: 0
                binding.tvCoins.text =
                    "${pref.giftCoin}"


            } catch (e: Exception) {
                Log.d("TAG", " exception on message " + e.message)
                e.printStackTrace()
            }

        }
    }

    override fun onIsTyping(vararg args: Any?) {

    }
    override fun onAnswerCall(vararg args: Any?) {
        Log.e("TAG", "onAnswerCall: .........${Gson().toJson(args[0])}")
    }
    override fun onEndCall(vararg args: Any?) {
        Log.e("TAG", "onEndCall: .........${Gson().toJson(args[0])}")
    }
    override fun onReceivedVideoGift(vararg args: Any?) {
        Log.e("TAG", "onReceivedVideoGift: .........${Gson().toJson(args[0])}")
    }

    override fun onPremiumBroadcastMinute(vararg args: Any?) {
        Log.e("TAG", "onPremiumBroadcastMinute: .........${Gson().toJson(args[0])}")
        var coin = Gson().toJson(args[0])
        println("==================coin$coin")
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

    fun roomJoin() {
        try {
            if (isRoomJoin) {
                val `object` = JSONObject()
                `object`.put(SocketConstants.BROADCASTER, pref.broadcastId)
                mSocket?.emit(SocketConstants.JOIN_BROADCAST_ROOM, `object`)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun decodeBinaryDataToString(data: ByteArray, charset: Charset = Charsets.UTF_8): String {
        return try {
            String(data, charset)
        } catch (e: Exception) {
            Log.e("DecodeError", "Failed to decode binary data to string", e)
            ""
        }
    }

    override fun onSignalingEvent(type: String, event: Any) {
        runOnUiThread {
            if (event is MessageEvent) {
                try {
                    val data = (event.message.data as ByteArray)
                    val decodedData = decodeBinaryDataToString(data)
                    val messageData = Gson().fromJson(decodedData, AgoraSendData::class.java)
                    println("BaseLiveStreamingActivi$messageData")
                    commentsAdapter.add(
                        AgoraSendData(
                            messageData.ProfileImgUrl,
                            messageData?.message ?: "",
                            messageData?.sender ?: ""
                        )
                    )
                    binding.rvComments.scrollToPosition(commentsAdapter.itemCount - 1)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }
}