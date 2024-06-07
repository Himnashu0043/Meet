package org.meetcute.network.data.api.auth

import org.meetcute.network.data.model.GetAllMediaResponse
import org.meetcute.network.data.model.Post
import org.meetcute.network.data.model.UserResponse
import org.meetcute.network.data.model.ValidateUserResponse
import retrofit2.Response

interface Auth {

    suspend fun getProfile(jwtToken:String):Response<UserResponse>
    suspend fun validationAndSendOtp(email: String): Response<ValidateUserResponse>

    suspend fun signUp(email: String, password: String): Response<UserResponse>

    suspend fun login(email: String, password: String): Response<UserResponse>

    suspend fun basicInfo(
        name: String,
        username: String,
        maleSelected: Boolean,
        country: String,
        year: String,
        month: String, day: String,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun uploadProfile(
        profileImage: String,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun additionalInfo(
        aboutYourSelf: String,
        figureType: String,
        language: String,
        height: String,
        weight: String,
        profession: String,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun uploadImagesVideosResponse(
        arrayList: MutableList<Post?>,
        thumbnail: String,
        selfiesVideo: String,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun updateHashTag(
        arrayList: MutableList<String?>,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun setVideoCallRate(
        videoCallRate: String,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun bankAccount(
        bankName: String,
        accountHolderName: String,
        accountNo: String,
        ifscCode: String,
        isEditMode: Boolean,jwtToken:String
    ): Response<UserResponse>

    suspend fun getAllMedia(jwtToken:String
    ): Response<GetAllMediaResponse>

    suspend fun deletePost(
        id: String,jwtToken:String
    ): Response<GetAllMediaResponse>


}