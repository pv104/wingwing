package com.ssafy.shieldroneapp.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import android.util.Log

/**
 * WebSocket 연결을 관리하는 클래스.
 *
 * WebSocket 연결 설정 및 해제, 연결 상태를 확인하는 기능을 제공
 *
 * 주요 메서드
 * - connect(): WebSocket 서버에 연결
 * - disconnect(): WebSocket 연결을 해제
 * - isConnected(): WebSocket 연결 상태를 반환
 * - handleReconnect(): 연결 끊김 시 재연결 처리
 *
 * 이 클래스는 WebSocketService에 의해 import됩니다.
 *
 * 추가적으로 다음 클래스들과 협력합니다.
 * - WebSocketConfig: 설정 값을 가져와 연결 옵션(예: 재연결 간격, 타임아웃)을 설정
 * - WebSocketErrorHandler: 연결 중 발생하는 오류를 처리
 */

class WebSocketConnectionManager(
    private val webSocketService: WebSocketService
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder().url(WebSocketConfig.SERVER_URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "Connected to the server")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e("WebSocket", "Connection failed", t)
                webSocketService.handleReconnect()
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnecting")
        webSocket = null
    }

    fun isConnected(): Boolean {
        return webSocket != null
    }
}