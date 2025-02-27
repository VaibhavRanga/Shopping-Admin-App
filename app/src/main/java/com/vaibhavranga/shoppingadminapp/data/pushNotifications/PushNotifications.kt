package com.vaibhavranga.shoppingadminapp.data.pushNotifications

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhavranga.shoppingadminapp.R
import com.vaibhavranga.shoppingadminapp.common.USER_FCM_TOKEN_PATH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class PushNotifications @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val context: Context
) {
    private val client = OkHttpClient()
    private var accessToken = ""
    private val coroutine = CoroutineScope(Dispatchers.IO)

    init {
        coroutine.launch {
            accessToken()
        }
    }

    private suspend fun getTokens(): List<String> = withContext(Dispatchers.IO) {
        val snapshot = firebaseFirestore.collection(USER_FCM_TOKEN_PATH).get().await()
        snapshot.mapNotNull {
            it.getString("token")
        }
    }

    private suspend fun accessToken() {
        withContext(Dispatchers.IO) {
            try {
                val stream = context.resources.openRawResource(R.raw.account_key)
                val credential = GoogleCredentials
                    .fromStream(stream)
                    .createScoped("https://www.googleapis.com/auth/firebase.messaging")
                credential.refresh()
                accessToken = credential.accessToken.tokenValue
            } catch (e: Exception) {
                Log.d("TAG", "accessToken: ${e.message}")
            }
        }
    }

    private suspend fun sendNotification(
        tokens: List<String>,
        productName: String,
        imageUrl: String
    ) {
        tokens.forEach { token ->
            val json = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", token)
                    put("notification", JSONObject().apply {
                        put("title", "New product added")
                        put("body", "Check out new product: $productName")
                        put("image", imageUrl)
                    })
                })
            }
            val body = json.toString().toRequestBody("application/json, charset=utf-8".toMediaTypeOrNull())
            val request = Request
                .Builder()
                .header("Authorization", "Bearer $accessToken")
                .url("https://fcm.googleapis.com/v1/projects/shopping-app-4ae90/messages:send")
                .post(body)
                .build()

            try {
                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }
                if (response.isSuccessful) {
                    Log.d("TAG", "sendNotification: ${response.body.toString()}")
                }
            } catch (e: Exception) {
                Log.d("TAG", "sendNotification: error: ${e.message.toString()}")
            }
        }
    }

    fun sendNotificationToAllUsers(
        productName: String,
        imageUrl: String
    ) {
        coroutine.launch {
            try {
                val tokens = getTokens()
                if (tokens.isNotEmpty()) {
                    sendNotification(
                        tokens = tokens,
                        productName = productName,
                        imageUrl = imageUrl
                    )
                }
            } catch (e: Exception) {
                Log.d("TAG", "sendNotificationToAllUsers: ${e.message.toString()}")
            }
        }
    }
}