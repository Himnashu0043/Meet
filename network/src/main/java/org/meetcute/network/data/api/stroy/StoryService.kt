package org.meetcute.network.data.api.stroy

import org.meetcute.network.data.model.AllStoryResponse
import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.CreateStoryResponse
import org.meetcute.network.data.model.InfoResponse
import org.meetcute.network.data.model.ReportViewer
import org.meetcute.network.data.model.StorySeenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface StoryService {

    @GET("broadcaster/getAllStories")
    suspend fun getAllStories(@Header("Authorization") jwtToken:String):Response<AllStoryResponse>


    @POST("broadcaster/addStories")
    suspend fun createStory(@Body hashMap: HashMap<String,Any>,@Header("Authorization") jwtToken:String):Response<CreateStoryResponse>

    @GET("broadcaster/seenStories")
    suspend fun seenStory(@Query("storyId") storyId:String,@Header("Authorization") jwtToken:String):Response<StorySeenResponse>

}