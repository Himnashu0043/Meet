package org.meetcute.viewModel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.meetcute.network.data.NetworkResponse
import org.meetcute.network.data.model.UserResponse
import org.meetcute.network.data.api.auth.Auth
import org.meetcute.network.data.api.stroy.Story
import org.meetcute.network.data.model.AllStoryResponse
import org.meetcute.network.data.model.CreateStoryResponse
import org.meetcute.network.data.model.FileType
import org.meetcute.network.di.AuthImpl1
import org.meetcute.network.di.StoryImpl1
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @AuthImpl1 private val auth: Auth,
    @StoryImpl1 private val story: Story
) : BaseViewModel() {

    val profileResponse = MutableLiveData<NetworkResponse<UserResponse>?>()
    fun getProfile() {
        io {
            profileResponse.value = api {
                auth.getProfile(
                    pref.user?.jwtToken ?: ""
                )
            }
        }
    }

    val allStoriesResponse = MutableLiveData<NetworkResponse<AllStoryResponse>?>()
    fun getAllStories() {
        io {
            allStoriesResponse.value = NetworkResponse.Loading
            allStoriesResponse.value = api {
                story.getAllStories(
                    pref.user?.jwtToken ?: ""
                )
            }
        }
    }


    val addStoriesResponse = MutableLiveData<NetworkResponse<CreateStoryResponse>?>()
    fun addStory(fileType: FileType, fileUrl: String, broadcasterId: String) {
        io {
            addStoriesResponse.value = NetworkResponse.Loading
            addStoriesResponse.value = api {
                story.createStory(fileType, fileUrl, broadcasterId, pref.user?.jwtToken ?: "")
            }
        }
    }

}