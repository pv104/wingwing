package com.ssafy.shieldroneapp.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.offset
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.shieldroneapp.R
import com.ssafy.shieldroneapp.data.source.local.UserLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun WatchConnectionBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(true) }

    // 이미지 애니메이션
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        delay(5000)
        visible = false
        delay(300)
        onDismiss()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(15.dp),
                elevation = 8.dp,
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "나의 Galaxy Watch 6와 연동 성공",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.watch_ic),
                        contentDescription = "Galaxy Watch Icon",
                        modifier = Modifier
                            .size(144.dp)
                            .offset(y = offset.dp)
                    )
                }
            }
        }
    }
}

/**
 * ConnectionState를 관리하는 sealed class
 */
sealed class WatchConnectionUiState {
    object Hidden : WatchConnectionUiState()
    data class Visible(val userName: String) : WatchConnectionUiState()
}

/**
 * WatchConnectionState를 관리하는 ViewModel
 */
@HiltViewModel
class WatchConnectionViewModel @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
) : ViewModel() {
    private val _uiState = MutableStateFlow<WatchConnectionUiState>(WatchConnectionUiState.Hidden)
    val uiState: StateFlow<WatchConnectionUiState> = _uiState.asStateFlow()

    fun showConnection() {
        viewModelScope.launch {
            try {
                val user = userLocalDataSource.getUser()
                val userName = user?.username ?: "사용자"
                _uiState.value = WatchConnectionUiState.Visible(userName)
                delay(5000)
                _uiState.value = WatchConnectionUiState.Hidden
            } catch (e: Exception) {
                Log.e("WatchConnection", "유저 정보 로드 실패", e)
                _uiState.value = WatchConnectionUiState.Visible("사용자")
                delay(5000)
                _uiState.value = WatchConnectionUiState.Hidden
            }
        }
    }

    fun hideConnection() {
        _uiState.value = WatchConnectionUiState.Hidden
    }
}
