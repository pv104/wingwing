package com.ssafy.shieldroneapp.services.connection

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import com.ssafy.shieldroneapp.utils.await

@Singleton
class WatchConnectionService @Inject constructor(
    private val context: Context
) {
    private val messageClient = Wearable.getMessageClient(context)
    private val nodeClient = Wearable.getNodeClient(context)

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    suspend fun checkConnection() {
        try {
            val nodes = nodeClient.connectedNodes.await()
            _isConnected.value = nodes.isNotEmpty()
            nodes.forEach { node ->
                Log.d(TAG, "Connected node: ${node.displayName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking connection", e)
            _isConnected.value = false
        }
    }

    companion object {
        private const val TAG = "WatchConnectionService"
    }
}