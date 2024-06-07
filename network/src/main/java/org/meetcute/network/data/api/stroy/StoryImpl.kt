package org.meetcute.network.data.api.stroy

import org.meetcute.network.data.model.AllStoryResponse
import org.meetcute.network.data.model.BlockListResponse
import org.meetcute.network.data.model.CreateStoryResponse
import org.meetcute.network.data.model.FileType
import org.meetcute.network.data.model.StorySeenResponse
import retrofit2.Response


class StoryImpl(private val story: StoryService) : Story {

    override suspend fun getAllStories(jwtToken: String): Response<AllStoryResponse> {
        return story.getAllStories(jwtToken)
    }

    override suspend fun createStory(
        fileType: FileType,
        fileUrl: String,
        broadcasterId: String,
        jwtToken: String
    ): Response<CreateStoryResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["fileType"] = fileType.name
        hashMap["fileUrl"] = fileUrl
        hashMap["broadcasterId"] = broadcasterId
        return story.createStory(hashMap, jwtToken)
    }

    override suspend fun seenStory(storyId: String, jwtToken: String): Response<StorySeenResponse> {
        return story.seenStory(storyId, jwtToken)
    }
}