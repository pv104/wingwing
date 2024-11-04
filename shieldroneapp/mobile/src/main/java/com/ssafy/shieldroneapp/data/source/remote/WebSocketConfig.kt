package com.ssafy.shieldroneapp.data.source.remote

/**
 * WebSocket 설정을 관리하는 클래스.
 *
 * WebSocket 연결에 필요한 설정 값을 정의
 *
 * 주요 메서드
 * - getWebSocketUrl(): WebSocket 서버 URL 반환
 * - getReconnectInterval(): 재연결 시도 간격 설정
 * - getTimeout(): 연결 타임아웃 설정 값 반환 (필요할 경우)
 *
 * 이 클래스는 WebSocketConnectionManager에 의해 import되며,
 * WebSocketConnectionManager를 통해 WebSocketService와 간접적으로 연결됩니다.
 */

class WebSocketConfig {
    companion object {
        const val SERVER_URL = "ws://localhost:8080/shieldrone"
        private const val TIMEOUT = 10000L // (ms)
        private const val RECONNECT_INTERVAL = 5000L // (ms)

        fun getWebSocketUrl(): String {
            return SERVER_URL
        }

        fun getReconnectInterval(): Long {
            return RECONNECT_INTERVAL
        }

        fun getTimeout(): Long {
            return TIMEOUT
        }
    }
}