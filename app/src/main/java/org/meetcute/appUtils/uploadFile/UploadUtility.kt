package org.meetcute.appUtils.uploadFile

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import org.meetcute.BuildConfig
import org.meetcute.appUtils.Loaders
import java.io.File
import kotlin.math.roundToInt

class UploadUtility(private val context: Context) {

    private var transferUtility: TransferUtility? = null
    private var client: AmazonS3Client? = null

    init {
        TransferNetworkLossHandler.getInstance(context)
        val credential = BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_KEY)
        client = AmazonS3Client(credential, Region.getRegion(BuildConfig.REGION))
        val tuOptions = TransferUtilityOptions()
        tuOptions.transferThreadPoolSize = 10
        transferUtility =
            TransferUtility.builder().s3Client(client).context(context.applicationContext)
                .transferUtilityOptions(tuOptions).build()
    }


    fun uploadFile(fileUri: String, onComplete: (String) -> Unit) {
        Loaders.show(context)
        val file = File(fileUri)
        val observer = transferUtility?.upload(
            BuildConfig.BUCKET_PATH,
            BuildConfig.DIR_NAME + file.name, file
        )
        observer?.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state != TransferState.IN_PROGRESS) {
                    when (state) {
                        TransferState.FAILED -> {
                            Loaders.hide()
                        }

                        TransferState.COMPLETED -> {
                            Loaders.hide()
                            val url =
                                BuildConfig.BASE_IMAGE_URL.plus(BuildConfig.DIR_NAME + file.name)
                            println("s3 uploaded image url $url")
                            onComplete.invoke(url)
                        }

                        else -> {

                        }
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentage = ((bytesCurrent.toDouble() / bytesTotal.toDouble()) * 100).toFloat()
            }

            override fun onError(id: Int, ex: java.lang.Exception?) {
                Loaders.hide()
            }

        })
    }

    fun uploadFile(file: File?, onComplete: (String) -> Unit) {
        Loaders.show(context)
        val observer = transferUtility?.upload(
            BuildConfig.BUCKET_PATH,
            BuildConfig.DIR_NAME + file?.name, file
        )
        observer?.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state != TransferState.IN_PROGRESS) {
                    when (state) {
                        TransferState.FAILED -> {
                            Loaders.hide()
                        }

                        TransferState.COMPLETED -> {
                            Loaders.hide()
                            val url =
                                BuildConfig.BASE_IMAGE_URL.plus(BuildConfig.DIR_NAME + file?.name)
                            println("s3 uploaded image url $url")
                            onComplete.invoke(url)
                        }

                        else -> {

                        }
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentage = ((bytesCurrent.toDouble() / bytesTotal.toDouble()) * 100).toFloat()
            }

            override fun onError(id: Int, ex: java.lang.Exception?) {
                Loaders.hide()
            }

        })
    }

    fun uploadFile(
        files: ArrayList<String>,
        progressChanged: (Int) -> Unit = { _ -> },
        fileUploaded: (String, Int) -> Unit
    ) {
        val arrayList = arrayListOf<Float>()
        for (index in 0 until files.count()) {
            val file = File(files[index])
            arrayList.add(0F)
            val observer = transferUtility?.upload(
                BuildConfig.BUCKET_PATH,
                BuildConfig.DIR_NAME + file.name, file
            )
            observer?.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState?) {
                    if (state != TransferState.IN_PROGRESS) {
                        when (state) {
                            TransferState.COMPLETED -> {
                                val url =
                                    BuildConfig.BASE_IMAGE_URL.plus(BuildConfig.DIR_NAME + file.name)
                                fileUploaded.invoke(url, index)
                            }

                            else -> {

                            }
                        }
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    arrayList[index] =
                        ((bytesCurrent.toDouble() / bytesTotal.toDouble()) * 100).toFloat()
                    progressChanged.invoke(((arrayList.sum() / arrayList.count())).roundToInt())
                }

                override fun onError(id: Int, ex: java.lang.Exception?) {

                }

            })
        }

    }


    fun uploadFile(
        files: ArrayList<File?>,
        fileUploaded: (String, Int) -> Unit
    ) {
        val arrayList = arrayListOf<Float>()
        for (index in 0 until files.count()) {
            val file = files[index]
            arrayList.add(0F)
            val observer = transferUtility?.upload(
                BuildConfig.BUCKET_PATH,
                BuildConfig.DIR_NAME + file?.name, file
            )
            observer?.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState?) {
                    if (state != TransferState.IN_PROGRESS) {
                        when (state) {
                            TransferState.COMPLETED -> {
                                val url =
                                    BuildConfig.BASE_IMAGE_URL.plus(BuildConfig.DIR_NAME + file?.name)
                                fileUploaded.invoke(url, index)
                            }

                            else -> {

                            }
                        }
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    arrayList[index] =
                        ((bytesCurrent.toDouble() / bytesTotal.toDouble()) * 100).toFloat()
//                    progressChanged.invoke(((arrayList.sum() / arrayList.count())).roundToInt())
                }

                override fun onError(id: Int, ex: java.lang.Exception?) {

                }

            })
        }

    }


}