package org.meetcute.network.data.api.auth

import org.meetcute.network.data.model.GetAllMediaResponse
import org.meetcute.network.data.model.Post
import org.meetcute.network.data.model.UserResponse
import org.meetcute.network.data.model.ValidateUserResponse
import retrofit2.Response
import javax.inject.Inject

class AuthImpl(private val auth: AuthService): Auth {
    override suspend fun getProfile(jwtToken:String): Response<UserResponse> {
        return auth.getUser(jwtToken)
    }

    override suspend fun validationAndSendOtp(email: String): Response<ValidateUserResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["email"] = email
        return auth.validationAndSendOtp(hashMap)
    }

    override suspend fun signUp(email: String, password: String): Response<UserResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["email"] = email
        hashMap["password"] = password
        return auth.signUp(hashMap)
    }

    override suspend fun login(email: String, password: String): Response<UserResponse> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["email"] = email
        hashMap["password"] = password
        return auth.login(hashMap)
    }



    private fun String.getMonthAsInt(): Int {
        return when (this) {
            "Jan" -> 1
            "Fab" -> 2
            "Mar" -> 3
            "Apr" -> 4
            "May" -> 5
            "Jun" -> 6
            "Jul" -> 7
            "Aug" -> 8
            "Sep" -> 9
            "Oct" -> 10
            "Nov" -> 11
            "Dec" -> 12
            else -> 1
        }
    }

    override suspend fun basicInfo(
        name: String,
        username: String,
        maleSelected: Boolean,
        country: String,
        year: String,
        month: String,day:String,
        isEditMode:Boolean,jwtToken:String): Response<UserResponse>
    {
        val hashMap = hashMapOf<String, Any>()
        hashMap["name"] = name
        hashMap["username"] = username
        if (maleSelected)
            hashMap["gender"] = "Male"
        else hashMap["gender"] = "Female"
        hashMap["country"] = country
        hashMap["dob"] = "${year}-${month.getMonthAsInt()}-$day"
        if (!isEditMode)
            hashMap["lastStep"] = 1
        return auth.updateUser(hashMap,jwtToken)
    }

    override suspend fun uploadProfile(
        profileImage: String,
        isEditMode:Boolean,jwtToken:String): Response<UserResponse>
    {
        val hashMap = hashMapOf<String, Any>()
        hashMap["profileImage"] = profileImage
        if (!isEditMode)
            hashMap["lastStep"] = 2
        return auth.updateUser(hashMap,jwtToken)
    }

    override suspend fun additionalInfo(
        aboutYourSelf: String,
        figureType: String,
        language: String,
        height: String,
        weight: String,
        profession: String,
        isEditMode:Boolean,jwtToken:String): Response<UserResponse>
    {
        val hashMap = hashMapOf<String, Any>()
        hashMap["aboutYourSelf"] = aboutYourSelf
        hashMap["figureType"] = figureType
        hashMap["language"] = language
        hashMap["height"] = height
        hashMap["weight"] = weight
        hashMap["profession"] = profession
        if (!isEditMode)
            hashMap["lastStep"] = 3
        return auth.updateUser(hashMap,jwtToken)
    }


    private fun getThumbnailVideo(url: String): HashMap<String, Any> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["fileType"] = "Video"
        hashMap["fileUrl"] = url
        hashMap["mediaType"] = "Thumbnail_Video"
        return hashMap
    }

    private fun getSelfieVideo(url: String): HashMap<String, Any> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["fileType"] = "Video"
        hashMap["fileUrl"] = url
        hashMap["mediaType"] = "Self_Loop_Video"
        return hashMap
    }

    private fun getPhoto(url: String): HashMap<String, Any> {
        val hashMap = hashMapOf<String, Any>()
        hashMap["fileType"] = "Image"
        hashMap["fileUrl"] = url
        hashMap["mediaType"] = "Post"
        return hashMap
    }

    override suspend fun uploadImagesVideosResponse(
        arrayList: MutableList<Post?>,
        thumbnail: String,
        selfiesVideo: String,
        isEditMode:Boolean,jwtToken:String
    ):Response<UserResponse>{
        val hashMap = hashMapOf<String, Any>()
        val array = arrayListOf(getThumbnailVideo(thumbnail), getSelfieVideo(selfiesVideo))
        arrayList.forEach {
            array.add(getPhoto(it?.fileUrl ?: ""))
        }
        hashMap["media"] = array
        if (!isEditMode)
            hashMap["lastStep"] = 4
        return auth.updateUser(hashMap,jwtToken)
    }

    override suspend fun updateHashTag(
        arrayList: MutableList<String?>,
        isEditMode:Boolean,jwtToken:String
    ):Response<UserResponse>{
        val hashMap = hashMapOf<String, Any>()
        hashMap["hashTags"] = arrayList
        if (!isEditMode)
            hashMap["lastStep"] = 5
        return auth.updateUser(hashMap,jwtToken)
    }

    override suspend fun setVideoCallRate(
        videoCallRate: String,
        isEditMode:Boolean,jwtToken:String
    ):Response<UserResponse>{
        val hashMap = hashMapOf<String, Any>()
        hashMap["videoCallRate"] = videoCallRate.toInt()
        if (!isEditMode)
            hashMap["lastStep"] = 6
        return auth.updateUser(hashMap,jwtToken)
    }

    override suspend fun bankAccount(
        bankName: String,
        accountHolderName: String,
        accountNo: String,
        ifscCode: String,
        isEditMode:Boolean,jwtToken:String
    ):Response<UserResponse>{
        val hashMap = hashMapOf<String, Any>()
        hashMap["bankName"] = bankName
        hashMap["accountHolderName"] = accountHolderName
        hashMap["accountNo"] = accountNo
        hashMap["ifscCode"] = ifscCode
        val bankHashMap = hashMapOf<String, Any>()
        bankHashMap["bank"] = hashMap
        if (!isEditMode)
            bankHashMap["lastStep"] = 7
        return auth.updateUser(bankHashMap,jwtToken)
    }

    override suspend fun getAllMedia(jwtToken:String
    ):Response<GetAllMediaResponse>{
        return auth.getAllMedia(jwtToken)
    }

    override suspend fun deletePost(id:String,jwtToken:String
    ):Response<GetAllMediaResponse>{
        return auth.deletePost(id,jwtToken)
    }


}