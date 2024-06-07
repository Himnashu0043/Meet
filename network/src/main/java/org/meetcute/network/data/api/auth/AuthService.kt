package org.meetcute.network.data.api.auth

import org.meetcute.network.data.model.GetAllMediaResponse
import org.meetcute.network.data.model.UserResponse
import org.meetcute.network.data.model.ValidateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthService {

    @POST("broadcaster/validationAndSendOtp")
    suspend fun validationAndSendOtp(@Body hashMap: HashMap<String, Any>): Response<ValidateUserResponse>

    @POST("broadcaster/signup")
    suspend fun signUp(@Body hashMap: HashMap<String, Any>): Response<UserResponse>

    @POST("broadcaster/login")
    suspend fun login(@Body hashMap: HashMap<String, Any>): Response<UserResponse>

    @GET("broadcaster/getBroadcasterProfile")
    suspend fun getUser(
        @Header("Authorization") auth: String ,
    ): Response<UserResponse>

    @PUT("broadcaster/updateProfile")
    suspend fun updateUser(
        @Body body: HashMap<String, Any>,
        @Header("Authorization") auth: String
    ): Response<UserResponse>

    @GET("broadcaster/getAllMedia")
    suspend fun getAllMedia(
        @Header("Authorization") auth: String
    ): Response<GetAllMediaResponse>

    @HTTP(method = "DELETE", path = "broadcaster/deleteMediaById", hasBody = false)
//    @DELETE("deleteMediaById")
    suspend fun deletePost(
        @Query("id") id:String?,
        @Header("Authorization") auth: String
    ): Response<GetAllMediaResponse>



}