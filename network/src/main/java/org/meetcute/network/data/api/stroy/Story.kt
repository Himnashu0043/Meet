package org.meetcute.network.data.api.stroy

import org.meetcute.network.data.model.AllStoryResponse
import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.CreateStoryResponse
import org.meetcute.network.data.model.FileType
import org.meetcute.network.data.model.StorySeenResponse
import retrofit2.Response
import retrofit2.http.Query

interface Story {

    suspend fun getAllStories(jwtToken: String): Response<AllStoryResponse>

    suspend fun createStory(fileType: FileType,fileUrl:String,broadcasterId:String, jwtToken: String):Response<CreateStoryResponse>

    suspend fun seenStory(storyId:String,jwtToken:String):Response<StorySeenResponse>

}