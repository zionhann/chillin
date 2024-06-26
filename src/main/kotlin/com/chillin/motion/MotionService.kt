package com.chillin.motion

import com.chillin.http.HttpClient
import com.chillin.motion.request.MotionType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MotionService(
    private val httpClient: HttpClient,
    @Value("\${custom.motion.url}") private val baseUrl: String,
) {
    fun addMotion(pathname: String, fileData: ByteArray, motionType: MotionType): ByteArray {
        val formData = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", pathname, fileData.toRequestBody())
            .addFormDataPart("motionType", motionType.value)
            .build()

        val request = Request.Builder()
            .post(formData)
            .url("$baseUrl/motion/")
            .build()

        val gifBytes = httpClient.call(request, 5L, TimeUnit.MINUTES).use { response ->
            response.body?.bytes() ?: throw RuntimeException("Failed to receive response from motion service.")
        }
        return gifBytes
    }
}