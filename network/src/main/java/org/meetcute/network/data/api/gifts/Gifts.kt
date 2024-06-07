package org.meetcute.network.data.api.gifts

import org.meetcute.network.data.model.GetAllGiftsResponse
import org.meetcute.network.data.model.TagGiftResponse
import retrofit2.Response

interface Gifts {

    suspend fun getAllGifts(jwtToken: String): Response<GetAllGiftsResponse>

    suspend fun tagGift(
        id: String,
        giftId: String,
        mileStone: String,
        message: String,
        jwtToken: String
    ): Response<TagGiftResponse>
}