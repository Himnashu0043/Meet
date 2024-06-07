package org.meetcute.appUtils.common

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.material.snackbar.Snackbar
import com.tooltip.Tooltip
import org.meetcute.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.absoluteValue


object Utils {


    private var sDownloadCache: SimpleCache? = null
    private const val maxCacheSize: Long = 100 * 1024 * 1024

    fun getInstance(context: Context): SimpleCache {
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        if (sDownloadCache == null) sDownloadCache = SimpleCache(File(context.cacheDir, "koko-media"), evictor)
        return sDownloadCache as SimpleCache
    }

    object ExoPlayerCacheSingleton {
        lateinit var cacheDataSourceFactory: CacheDataSource.Factory

        fun initializeCache(dataSourceFactory: CacheDataSource.Factory) {
            cacheDataSourceFactory = dataSourceFactory
        }
    }

    fun getTimeDifference(startTime:String,endTime:String):String{
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
             val start = ZonedDateTime.parse(startTime, dateTimeFormatter)
             val end = ZonedDateTime.parse(endTime, dateTimeFormatter)
             val duration = Duration.between(start, end)
             val hours = duration.toHours()
             val minutes = duration.toMinutes() % 60
             return "$hours hr $minutes min"
        } else {
             val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
             val date1 = dateFormat.parse(startTime)
             val date2 = dateFormat.parse(endTime)
             val durationInMillis = abs((date1?.time ?: 0 ) - (date2?.time?:0))
             val hours = durationInMillis / (1000 * 60 * 60)
             val minutes = (durationInMillis % (1000 * 60 * 60)) / (1000 * 60)
             return "$hours hr $minutes min"
        }
    }

    fun ImageView.load(url: String?) {
        Glide.with(this).load(url).into(this)
    }

    fun ImageView.loadCircleCrop(url: String?) {
        Glide.with(this).load(url).circleCrop().into(this)
    }


    fun parseDate(dateString: String): Triple<Int, String, Int> {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date: Date = inputFormat.parse(dateString)
            val dayFormat = SimpleDateFormat("dd")
            val monthFormat = SimpleDateFormat("MMM")
            val yearFormat = SimpleDateFormat("yyyy")
            val day: Int = dayFormat.format(date).toInt()
            val month = monthFormat.format(date).toString()
            val year: Int = yearFormat.format(date).toInt()
            println("Day: $day, Month: $month, Year: $year")
            return Triple(day, month, year)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Triple(0, "Jan", 0)

    }

    fun Int.dp(context: Context): Int {
        val metrics = context.resources.displayMetrics
        val dpValue = absoluteValue / (metrics.densityDpi / 160f)
        return dpValue.toInt()
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        var filePath: String? = null
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow("_data")
                    filePath = cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            filePath = uri.path
        }
        return if (filePath != null) {
            File(filePath)
        } else {
            null
        }
    }

    fun String?.show(binding: ViewBinding) {
        Snackbar.make(binding.root, toString(), Snackbar.ANIMATION_MODE_SLIDE).show()
    }
    fun timeToMilliseconds(timeString: String): Long {
        val parts = timeString.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()

        // Convert hours and minutes to milliseconds
        val totalMilliseconds = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000)

        return totalMilliseconds.toLong()
    }

    fun formatMillisToTime(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millis % (1000 * 60)) / 1000
        return String.format("%02dh:%02dm:%02ds", hours, minutes, seconds)
    }
    fun formatMillisToTime1(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millis % (1000 * 60)) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    fun timeStringToInt(timeString: String): Int {
        val parts = timeString.split("h", "m")
        val hours = parts[0].trim().toInt()
        val minutes = parts[1].trim().toInt()

        // Convert hours and minutes to total minutes
        val totalMinutes = hours * 60 + minutes

        return totalMinutes
    }

   fun View.showTooltip(text: String) {
        Tooltip.Builder(this)
            .setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            .setCornerRadius(4f)
            .setTextColor(Color.BLACK)
            .setCancelable(true)
            .setDismissOnClick(true)
            .setText(text)
            .show()
    }
    fun parseTimeFormat(time: String?): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        "2022-05-06T 09:25:59.629Z"
        val outputPattern = "HH:mm:ss"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
    fun parseTimeFormat1(time: String?): String? {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val outputPattern = "HH:mm"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
    fun getAge(date: String?): Int {
        var age = 0
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date1 = formatter.parse(date)
            val now = Calendar.getInstance()
            val dob = Calendar.getInstance()
            dob.time = date1
            require(!dob.after(now)) { "Can't be born in the future" }
            val year1 = now[Calendar.YEAR]
            val year2 = dob[Calendar.YEAR]
            age = year1 - year2
            val month1 = now[Calendar.MONTH]
            val month2 = dob[Calendar.MONTH]
            if (month2 > month1) {
                age--
            } else if (month1 == month2) {
                val day1 = now[Calendar.DAY_OF_MONTH]
                val day2 = dob[Calendar.DAY_OF_MONTH]
                if (day2 > day1) {
                    age--
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        Log.d("TAG", "getAge: AGE=> $age")
        return age
    }
    fun setFormatDateNew(originalDate: String?): String? {
        if (originalDate != "null") {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(originalDate)
            val formattedDate = outputFormat.format(date)
            return formattedDate
        }


        return ""

    }


    fun formatDuration(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return when {
            hours > 0 && remainingMinutes > 0 -> "$hours hr $remainingMinutes min"
            hours > 0 -> "$hours hr"
            else -> "$remainingMinutes min"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeAgo(timestamp: String): String {
        val now = Instant.now()
        val past = Instant.parse(timestamp)
        val duration = Duration.between(past, now)

        val seconds = duration.seconds

        return when {
            seconds < 60 -> "$seconds seconds ago"
            seconds < 3600 -> "${seconds / 60} minutes ago"
            seconds < 86400 -> "${seconds / 3600} hours ago"
            seconds < 604800 -> "${seconds / 86400} days ago"
            seconds < 2592000 -> "${seconds / 604800} weeks ago"
            seconds < 31536000 -> "${seconds / 2592000} months ago"
            else -> "${seconds / 31536000} years ago"
        }
    }
    fun formatDuration1(minutes: Int): String {
        val years = minutes / (60 * 24 * 365)
        val days = (minutes % (60 * 24 * 365)) / (60 * 24)
        val hours = (minutes % (60 * 24)) / 60
        val mins = minutes % 60

        val result = StringBuilder()

        if (years > 0) {
            result.append("$years yr")
            if (years > 1) result.append("s")
        }
        if (days > 0) {
            if (result.isNotEmpty()) result.append(" ")
            result.append("$days day")
            if (days > 1) result.append("s")
        }
        if (hours > 0) {
            if (result.isNotEmpty()) result.append(" ")
            result.append("$hours hr")
            if (hours > 1) result.append("s")
        }
        if (mins > 0) {
            if (result.isNotEmpty()) result.append(" ")
            result.append("$mins min")
            if (mins > 1) result.append("s")
        }

        return result.toString()
    }

}