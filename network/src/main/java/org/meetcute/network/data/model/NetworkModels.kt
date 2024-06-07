package org.meetcute.network.data.model

import java.io.Serializable


data class ErrorModel(
    val message: String,
    val success: Boolean
)

data class ValidateUserResponse(
    val `data`: ValidateData,
    val message: String,
    val success: Boolean
)

data class ValidateData(
    val isExist: Boolean,
    val otp: String
)


// SignUpResponse

data class UserResponse(
    val `data`: User,
    val message: String,
    val success: Boolean
)

//@Entity("user_table")
data class User(
    val __v: Int,
    val _id: String,
    val aboutYourSelf: String,
    val active: Boolean,
    val age: String,
    val approvalType: String,
    val bank: Bank,
    val broadcater_id: String,
    val commissionType: String,
    val commissionValue: Int,
    val country: String?,
    val createdAt: String,
    val delete: Boolean,
    val disApprovedReason: String,
    val dob: String?,
    val email: String,
    val fcmToken: FcmToken,
    val figureType: String,
    val followers: Int,
    val gender: String,
    val hashTags: List<String>,
    val height: String,
    val isAboutEdited: Boolean,
    val isApproved: Boolean,
    val performerMode: Boolean,
    val isBankDetailEdited: Boolean,
    val isBasicInfoEdited: Boolean,
    val isHashtagsEdited: Boolean,
    val isLoopVideoEdited: Boolean,
    val isPostImagesEdited: Boolean,
    val isProfileEdited: Boolean,
    val isProfileImageEdited: Boolean,
    val isStoriesEdited: Boolean,
    val isVerfied: Boolean,
    val jwtToken: String,
    val language: String,
    val lastStep: String?,
    val location: Location,
    val name: String,
    val password: String,
    val profession: String,
    val profileImage: String,
    val pushNotification: Boolean,
    val updatedAt: String,
    val username: String,
    val videoCallRate: Int,
    val wallet: Int,
    val weight: String
)

data class Bank(
    val accountHolderName: String,
    val accountNo: Long,
    val bankName: String,
    val ifscCode: String
)

data class FcmToken(
    val apnToken: String,
    val deviceToken: String,
    val deviceType: String
)

data class Location(
    val coordinates: List<Int>,
    val type: String
)


data class GetAllMediaResponse(
    val `data`: MediaData,
    val message: String,
    val success: Boolean
)

data class MediaData(
    val Post: List<Post>?,
    val Self_Loop_Video: List<SelfLoopVideo>?,
    val Thumbnail_Video: List<ThumbnailVideo>?
)

data class Post(
    val __v: Int,
    val _id: String,
    val active: Boolean,
    val broadcasterId: String,
    val createdAt: String,
    val delete: Boolean,
    val fileType: String,
    val fileUrl: String,
    val mediaType: String,
    val updatedAt: String
)

data class SelfLoopVideo(
    val __v: Int,
    val _id: String,
    val active: Boolean,
    val broadcasterId: String,
    val createdAt: String,
    val delete: Boolean,
    val fileType: String,
    val fileUrl: String,
    val mediaType: String,
    val updatedAt: String
)

data class ThumbnailVideo(
    val __v: Int,
    val _id: String,
    val active: Boolean,
    val broadcasterId: String,
    val createdAt: String,
    val delete: Boolean,
    val fileType: String,
    val fileUrl: String,
    val mediaType: String,
    val updatedAt: String
)


//  Broadcast History


data class BroadcastHistoryResponse(
    val data: List<BroadcastHistoryData>,
    val message: String,
    val success: Boolean
)

data class BroadcastHistoryData(
    val RTCToken: String,
    val __v: Int,
    val _id: String,
    val broadcasterId: String,
    val canChat: String,
    val canJoin: String,
    val channelName: String,
    val coin: Int,
    val createdAt: String,
    val currentViews: Int,
    val duration: Int,
    val endTime: String,
    val giftEarning: Int,
    val giftSound: Boolean,
    val isContestStart: Boolean,
    val isPremium: Boolean,
    val mic: Boolean,
    val notificationText: String,
    val premiumEarning: Int,
    val premiumGiftsEarning: Int,
    val premiumPrice: Int,
    val premiumViews: Int,
    val startTime: String,
    val status: String,
    val thumbnail: String,
    val totalEarning: Int,
    val totalViews: Int,
    val uid: Int,
    val updatedAt: String
)


// start broadcast response

data class StartBroadcastResponse(
    val data: StartBroadcastData,
    val message: String,
    val success: Boolean
)

data class StartBroadcastData(
    val RTCToken: String,
    val __v: Int,
    val _id: String,
    val broadcasterId: String,
    val canChat: String,
    val canJoin: String,
    val channelName: String,
    val coin: Int,
    val createdAt: String,
    val currentViews: Int,
    val duration: Int,
    val giftEarning: Int,
    val giftSound: Boolean,
    val isContestStart: Boolean,
    val isPremium: Boolean,
    val mic: Boolean,
    val notificationText: String,
    val premiumEarning: Int,
    val premiumGiftsEarning: Int,
    val premiumPrice: Int,
    val premiumViews: Int,
    val startTime: String,
    val status: String,
    val thumbnail: String,
    val totalEarning: Int,
    val totalViews: Int,
    val uid: Int,
    val updatedAt: String
)


// coin price range response
data class CoinPriceRangeResponse(
    val `data`: CoinPriceRangeData,
    val message: String,
    val success: Boolean
)

data class CoinPriceRangeData(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val premiumBroadcast: PremiumBroadcast,
    val premiumDuration: PremiumDuration,
    val premiumVideoRange: PremiumVideoRange,
    val updatedAt: String,
    val videoCallRate: VideoCallRate
)

data class PremiumBroadcast(
    val active: Boolean,
    val fixedValue: Int,
    val lowerLimit: Int,
    val upperLimit: Int
)

data class PremiumDuration(
    val active: Boolean,
    val fixedValue: Int,
    val lowerLimit: Int,
    val upperLimit: Int
)

data class PremiumVideoRange(
    val active: Boolean,
    val fixedValue: Int,
    val lowerLimit: Int,
    val upperLimit: Int
)

data class VideoCallRate(
    val active: Boolean,
    val coin: Int,
    val coinValue: Int,
    val commission: Int,
    val commissionAmount: Int
)


// block list

data class BlockListResponse(
    val `data`: List<BlockListItem>,
    val message: String,
    val success: Boolean
)

data class BlockListItem(
    val __v: Int,
    val _id: String,
    val broadcasterId: String,
    val createdAt: String,
    val type: String,
    val updatedAt: String,
    val viewer: BlockedViewer,
    val viewerId: String
)

data class BlockedViewer(
    val _id: String,
    val name: String,
    val viewer_id: String
)


// info response
data class InfoResponse(
    val message: String,
    val success: Boolean
)


// Report Viewer

data class ReportViewer(
    val `data`: ReportViewerData,
    val message: String,
    val success: Boolean
)

data class ReportViewerData(
    val __v: Int,
    val _id: String,
    val broadcasterId: String,
    val createdAt: String,
    val reason: String,
    val report_id: String,
    val type: String,
    val updatedAt: String,
    val viewerId: String
)

// TagGiftResponse
data class TagGiftResponse(
    val `data`: TagGiftData,
    val message: String,
    val success: Boolean
)

data class TagGiftData(
    val __v: Int,
    val _id: String,
    val canChat: String,
    val canJoin: String,
    val coin: Int,
    val createdAt: String,
    val currentViews: Int,
    val duration: Int,
    val giftEarning: Int,
    val giftSound: Boolean,
    val isContestStart: Boolean,
    val isPremium: Boolean,
    val mic: Boolean,
    val notificationText: String,
    val premiumEarning: Int,
    val premiumGiftsEarning: Int,
    val premiumPrice: Int,
    val premiumViews: Int,
    val status: String,
    val tagGift: TagGift,
    val thumbnail: String,
    val totalEarning: Int,
    val totalViews: Int,
    val updatedAt: String
)

data class TagGift(
    val giftId: String,
    val message: String,
    val mileStone: Int
)

// get all gifts

data class GetAllGiftsResponse(
    val `data`: List<GetAllGiftsData>,
    val message: String,
    val success: Boolean
)

data class GetAllGiftsData(
    val __v: Int,
    val _id: String,
    val active: Boolean,
    val createdAt: String,
    val delete: Boolean,
    val imageUrl: String,
    val largeImageUrl: String,
    val price: Int,
    val smallImageUrl: String,
    val updatedAt: String
)

// Agora Token Response

data class AgoraTokenResponse(
    val `data`: AgoraTokenData,
    val message: String,
    val success: Boolean
)

data class AgoraTokenData(
    val channelName: String,
    val rtcToken: String,
    val uid: Int
)

// chat list response

data class ChatListResponse(
    val `data`: List<ChatListItem>,
    val message: String,
    val success: Boolean
):Serializable

data class ChatListItem(
    val __v: Int,
    val _id: String,
    val active: Boolean,
    val broadcasterId: String,
    val broadcaster_count: Int,
    val createdAt: String,
    val lastMessage: LastMessage,
    var lastMessageAt: String,
    val updatedAt: String,
    val viewer: Viewer,
    val viewerId: String,
    var viewer_count: Int
):Serializable

data class LastMessage(
    var message: String?,
    var mtype: String?
) : Serializable
data class Viewer(
    val _id: String,
    val name: String,
    val profileImage: String,
    val following: Int,
    val online: Boolean
):Serializable


enum class FileType{
    Image,Video
}


// Get All story

data class AllStoryResponse(
    val `data`: List<StoryItem>,
    val message: String,
    val success: Boolean
)

data class StoryItem(
    val __v: Int,
    val _id: String,
    val broadcasterId: String,
    val createdAt: String,
    val delete: Boolean,
    val fileType: String,
    val fileUrl: String,
    val seen: Boolean,
    val updatedAt: String
)


data class CreateStoryResponse(
    val `data`: StoryItem,
    val message: String,
    val success: Boolean
)

data class StorySeenResponse(
    val `data`: StorySeen,
    val message: String,
    val success: Boolean
)

data class StorySeen(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val storyId: String,
    val updatedAt: String,
    val viewerId: String
)
////pending Video
data class VideoPendingRes(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var broadcasterId: String?,
        var broadcast: String?,
        var callStatus: String?,
        var createdAt: String?,
        var duration: Int?,
        var notify: Boolean?,
        var updatedAt: String?,
        var viewer: List<Viewer>?,
        var viewerId: String?
    ) {
        data class Viewer(
            var _id: String?,
            var name: String?,
            var profileImage: String?,
            var viewer_id: String?
        )
    }


}

// End broadcast response
data class EndBroardCastRes(
    var data: Any?,
    var message: String?,
    var success: Boolean?
)

///join user
data class JoinUserResponse(
    var data: Data?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var broadcasterId: String?,
        var broadcaster_count: Int?,
        var createdAt: String?,
        var isBroadcasterDelete: Boolean?,
        var isViewerDelete: Boolean?,
        var lastMessage: LastMessage?,
        var lastMessageAt: String?,
        var updatedAt: String?,
        var viewerId: String?,
        var viewer_count: Int?
    ) {
        data class LastMessage(
            var message: String?,
            var mtype: String?
        )
    }
}

/////top gifters
data class TopGifterRes(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var _id: String?,
        var broadcast: String?,
        var broadcasterId: String?,
        var coin: Int?,
        var viewer: List<Viewer>?,
        var viewerId: String?
    ) {
        data class Viewer(
            var _id: String?,
            var name: String?,
            var online: Boolean?,
            var profileImage: String?,
            var viewer_id: String?
        )
    }
}

////// top BroadCast Gift List
data class TopBroadCastGiftRes(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var _id: String?,
        var broadcast: String?,
        var broadcasterId: String?,
        var coin: Int?,
        var viewer: List<Viewer>?
    ) {
        data class Viewer(
            var _id: String?,
            var broadcaster_id: String?,
            var name: String?,
            var online: Boolean?,
            var profileImage: String?
        )
    }
}

//////chat MessageList
data class ChatMessageListRes(
    var data: List<ChatData>?,
    var message: String?,
    var success: Boolean?
) {

    class TypingData :
        ChatData(null, null, null, null, null, null, null, null, null, null, null, null)

    open class ChatData(
        var __v: Int?,
        var _id: String?,
        var broadcaster: Broadcaster?,
        var createdAt: String?,
        var isBroadcasterDelete: Boolean?,
        var isViewerDelete: Boolean?,
        var message: Message?,
        var read: Boolean?,
        var roomId: String?,
        var sentBy: String?,
        var updatedAt: String?,
        var viewer: Viewer?
    ) {
        data class Broadcaster(
            var _id: String?,
            var name: String?,
            var profileImage: String?
        )

        data class Message(
            var message: String?,
            var mtype: String?
        )

        data class Viewer(
            var _id: String?,
            var name: String?,
            var online: Boolean?,
            var profileImage: String?
        )
    }
}

data class ChatSocketResponse(
    var nameValuePairs: NameValuePairs?
) {
    data class NameValuePairs(
        var __v: Int?,
        var _id: String?,
        var broadcaster: Broadcaster?,
        var createdAt: String?,
        var isBroadcasterDelete: Boolean?,
        var isViewerDelete: Boolean?,
        var message: Message?,
        var read: Boolean?,
        var roomId: String?,
        var sentBy: String?,
        var updatedAt: String?,
        var viewer: Viewer?
    ) {
        data class Broadcaster(
            var nameValuePairs: NameValuePairs?
        ) {
            data class NameValuePairs(
                var _id: String?,
                var name: String?,
                var profileImage: String?
            )
        }

        data class Message(
            var nameValuePairs: NameValuePairs?
        ) {
            data class NameValuePairs(
                var message: String?,
                var mtype: String?
            )
        }

        data class Viewer(
            var nameValuePairs: NameValuePairs?
        ) {
            data class NameValuePairs(
                var _id: String?,
                var name: String?,
                var online: Boolean?,
                var profileImage: String?
            )
        }
    }
}

//////Get Wallet Transactions
data class GetWalletTransaction(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var amount: Int?,
        var broadcasterId: String?,
        var coin: Int?,
        var createdAt: String?,
        var currency: String?,
        var trans_id: String?,
        var type: String?,
        var updatedAt: String?
    )
}


////// View SocketResponse
data class ViewSocketResponse(
    var nameValuePairs: NameValuePairs?
) {
    data class NameValuePairs(
        var currentViews: Int?
    )
}

/////// Received Gift
data class ReceivedGiftResponse(
    var nameValuePairs: NameValuePairs?
) {
    data class NameValuePairs(
        var receivedGift: ReceivedGift?
    ) {
        data class ReceivedGift(
            var nameValuePairs: NameValuePairs?
        ) {
            data class NameValuePairs(
                var __v: Int?,
                var _id: String?,
                var broadcast: String?,
                var broadcasterId: String?,
                var coin: Int?,
                var createdAt: String?,
                var giftId: String?,
                var type: String?,
                var updatedAt: String?,
                var viewerId: String?
            )
        }
    }
}

/////////GiftByBroadcastListRes
data class GiftByBroadcastListRes(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var _id: String?,
        var broadcast: String?,
        var broadcasterId: String?,
        var coin: Int?,
        var viewer: List<Viewer>?,
        var viewerId: String?
    ) {
        data class Viewer(
            var _id: String?,
            var name: String?,
            var online: Boolean?,
            var profileImage: String?,
            var viewer_id: String?
        )
    }
}

/////// ContestUpdateRankingResponse

data class ContestUpdateRankingResponse(
    var values: List<Value>?
) {
    data class Value(
        var nameValuePairs: NameValuePairs?
    ) {
        data class NameValuePairs(
            var _id: String?,
            var broadcast: String?,
            var broadcasterId: String?,
            var coin: Int?,
            var createdAt: String?,
            var updatedAt: String?,
            var viewer: Viewer?,
            var viewerId: String?
        ) {
            data class Viewer(
                var values: List<Value>?
            ) {
                data class Value(
                    var nameValuePairs: NameValuePairs?
                ) {
                    data class NameValuePairs(
                        var __v: Int?,
                        var _id: String?,
                        var active: Boolean?,
                        var age: String?,
                        var createdAt: String?,
                        var delete: Boolean?,
                        var deviceId: String?,
                        var email: String?,
                        var fcmToken: FcmToken?,
                        var following: Int?,
                        var gender: String?,
                        var jwtToken: String?,
                        var lastActionAt: String?,
                        var lifeTimeSpend: Int?,
                        var location: Location?,
                        var name: String?,
                        var online: Boolean?,
                        var profileImage: String?,
                        var pushNotification: Boolean?,
                        var updatedAt: String?,
                        var verified: Boolean?,
                        var viewer_id: String?,
                        var wallet: Int?,
                        var welcomeBonus: Boolean?
                    ) {
                        data class FcmToken(
                            var nameValuePairs: NameValuePairs?
                        ) {
                            class NameValuePairs
                        }

                        data class Location(
                            var nameValuePairs: NameValuePairs?
                        ) {
                            data class NameValuePairs(
                                var coordinates: Coordinates?,
                                var type: String?
                            ) {
                                data class Coordinates(
                                    var values: List<Int?>?
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
////////Video Call Histroy
data class VideoCallHistoryResponse(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var broadcast: String?,
        var broadcasterId: String?,
        var callStatus: String?,
        var createdAt: String?,
        var duration: Int?,
        var endTime: String?,
        var notify: Boolean?,
        var updatedAt: String?,
        var videoCall_id: String?,
        var viewer: List<Viewer?>?,
        var viewerId: String?
    ) {
        data class Viewer(
            var _id: String?,
            var name: String?,
            var online: Boolean?,
            var profileImage: String?,
            var viewer_id: String?
        )
    }
}
////////GetBroadcastVideoCallRequest
data class GetBroadCastVideoCallRequestRes(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var broadcast: String?,
        var broadcasterId: String?,
        var callStatus: String?,
        var createdAt: String?,
        var duration: Int?,
        var notify: Boolean?,
        var updatedAt: String?,
        var videoCall_id: String?,
        var viewer: List<Viewer>?,
        var viewerId: String?
    ) {
        data class Viewer(
            var _id: String?,
            var name: String?,
            var online: Boolean?,
            var profileImage: String?,
            var viewer_id: String?
        )
    }
}

///////// set Premium
data class SetPremiumResponse(
    var data: Data?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var RTCToken: String?,
        var __v: Int?,
        var _id: String?,
        var broadcasterId: String?,
        var canChat: String?,
        var canJoin: String?,
        var channelName: String?,
        var coin: Int?,
        var createdAt: String?,
        var currentViews: Int?,
        var duration: Int?,
        var endTime: String?,
        var giftEarning: Int?,
        var giftSound: Boolean?,
        var isContestStart: Boolean?,
        var isPremium: Boolean?,
        var mic: Boolean?,
        var notificationText: String?,
        var premiumEarning: Int?,
        var premiumGiftsEarning: Int?,
        var premiumPrice: Int?,
        var premiumViews: Int?,
        var startTime: String?,
        var status: String?,
        var thumbnail: String?,
        var totalEarning: Int?,
        var totalViews: Int?,
        var uid: Int?,
        var updatedAt: String?
    )
}

/////////getRTM Token
data class GetRTMTokenRes(
    var `data`: Data?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var rtmToken: String?,
        var userAccount: String?
    )
}

///////agora Send Data
data class AgoraSendData(
    var ProfileImgUrl: String,
    var message: String,
    var sender: String
)

/////// Privacy and Term
data class PrivacyAndTermResponse(
    var data: List<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var description: String?,
        var is_delete: Boolean?,
        var title: String?,
        var updatedAt: String?
    )
}

///////Analytics
data class AnalyticsResponse(
    var data: Data?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var braodcastTime: Int?,
        var broadcasterEaring: Int?,
        var newFollowers: Int?,
        var timeSpendOnApp: Int?,
        var totalViewers: Int?,
        var videoCallEarning: Int?
    )
}

/////// Broadcast Earning Graph
data class BroadCastEarningGraphResponse(
    var data: ArrayList<Data>?,
    var message: String?,
    var success: Boolean?
) {
    data class Data(
        var _id: Any?,
        var count: Int?
    )
}



