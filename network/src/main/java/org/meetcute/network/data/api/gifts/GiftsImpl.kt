package org.meetcute.network.data.api.gifts

import org.meetcute.network.data.model.GetAllGiftsResponse
import org.meetcute.network.data.model.TagGiftResponse
import retrofit2.Response

class GiftsImpl(private val gifts: GiftsService) : Gifts {

    override suspend fun getAllGifts(jwtToken: String): Response<GetAllGiftsResponse> {
        return gifts.getAllGifts(jwtToken)
    }

    override suspend fun tagGift(
        id: String,
        giftId: String,
        mileStone: String,
        message: String,
        jwtToken: String
    ): Response<TagGiftResponse> {
        val hashMap = hashMapOf<String,Any>()
        hashMap["_id"] = id
        hashMap["giftId"] = giftId
        hashMap["mileStone"] = mileStone
        hashMap["message"] = message
        return gifts.tagGift(hashMap,jwtToken)
    }


}