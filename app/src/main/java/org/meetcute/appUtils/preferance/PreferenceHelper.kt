package org.meetcute.appUtils.preferance

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.meetcute.network.data.model.MediaData
import org.meetcute.network.data.model.User

class PreferenceHelper private constructor(mContext: Context) {


    private val sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPref = mContext.getSharedPreferences("MeetCutePreference", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    var user: User?
        get() = run {
            val userString = sharedPref.getString("user", "")
            if (!userString.isNullOrEmpty()) Gson().fromJson(userString, User::class.java)
            else null
        }
        set(value) {
            editor.putString("user", Gson().toJson(value)).apply()
        }

    var getCanJoinAnyFollow: String
        get() = sharedPref.getString("getCanJoinAnyFollow", "") ?: ""
        set(value) {
            editor.putString("getCanJoinAnyFollow", value).apply()
        }

    var getRTMToken: String
        get() = sharedPref.getString("getRTMToken", "") ?: ""
        set(value) {
            editor.putString("getRTMToken", value).apply()
        }

    var getCanChatAnyfollow: String
        get() = sharedPref.getString("getCanChatAnyfollow", "") ?: ""
        set(value) {
            editor.putString("getCanChatAnyfollow", value).apply()
        }
    var getmic: Boolean
        get() = sharedPref.getBoolean("getmic", true)
        set(value) {
            editor.putBoolean("getmic", value).apply()
        }
    var getsound: Boolean
        get() = sharedPref.getBoolean("getsound", true)
        set(value) {
            editor.putBoolean("getsound", value).apply()
        }

    var getEditText: String
        get() = sharedPref.getString("getEditText", "") ?: ""
        set(value) {
            editor.putString("getEditText", value).apply()
        }
    var broadcastId: String
        get() = sharedPref.getString("broadcastId", "") ?: ""
        set(value) {
            editor.putString("broadcastId", value).apply()
        }
    var channelName: String
        get() = sharedPref.getString("channelName", "") ?: ""
        set(value) {
            editor.putString("channelName", value).apply()
        }

    var videoId: String
        get() = sharedPref.getString("videoId", "") ?: ""
        set(value) {
            editor.putString("videoId", value).apply()
        }
    var viewerName: String
        get() = sharedPref.getString("viewerName", "") ?: ""
        set(value) {
            editor.putString("viewerName", value).apply()
        }
    var viewerProfile: String
        get() = sharedPref.getString("viewerProfile", "") ?: ""
        set(value) {
            editor.putString("viewerProfile", value).apply()
        }

    var giftCoin: Int
        get() = sharedPref.getInt("giftCoin", 0)
        set(value) {
            editor.putInt("giftCoin", value).apply()
        }

    var userMedia: MediaData?
        get() = run {
            val userString = sharedPref.getString("mediaData", "")
            if (!userString.isNullOrEmpty()) Gson().fromJson(userString, MediaData::class.java)
            else null
        }
        set(value) {
            editor.putString("mediaData", Gson().toJson(value)).apply()
        }

    fun logout() {
        editor.clear()
        editor.apply()
    }

    companion object {
        fun get(context: Context): PreferenceHelper {
            return PreferenceHelper(context)
        }
    }
}