package org.meetcute.network.data.api.gifts

import org.meetcute.network.data.model.GetAllGiftsResponse
import org.meetcute.network.data.model.TagGiftResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface GiftsService {

    @PUT("broadcaster/tagGiftBroadcast")
    suspend fun tagGift(@Body hashMap: HashMap<String,Any>,@Header("Authorization") jwtToken:String):Response<TagGiftResponse>

    @GET("broadcaster/getAllGifts")
    suspend fun getAllGifts(@Header("Authorization") jwtToken:String):Response<GetAllGiftsResponse>
}