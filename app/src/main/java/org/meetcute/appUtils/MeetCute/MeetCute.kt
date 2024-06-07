package org.meetcute.appUtils.MeetCute

import android.app.Application
import androidx.room.Room
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import dagger.hilt.android.HiltAndroidApp
import org.meetcute.appUtils.common.Utils
import org.meetcute.appUtils.common.Utils.getInstance
import org.meetcute.appUtils.preferance.PreferenceHelper
import javax.inject.Inject

@HiltAndroidApp
class MeetCute : Application() {

    lateinit var pref: PreferenceHelper
    override fun onCreate() {
        super.onCreate()
        app = this
        pref = PreferenceHelper.get(app)
        val upstreamFactory = DefaultDataSourceFactory(this, "user-agent")
        val cacheDataSourceFactory = CacheDataSource.Factory()
        cacheDataSourceFactory.setCache(getInstance(this))
        cacheDataSourceFactory.setUpstreamDataSourceFactory(upstreamFactory)
        cacheDataSourceFactory.setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        Utils.ExoPlayerCacheSingleton.initializeCache(cacheDataSourceFactory)
    }

    companion object{
        lateinit var app:MeetCute
    }

}