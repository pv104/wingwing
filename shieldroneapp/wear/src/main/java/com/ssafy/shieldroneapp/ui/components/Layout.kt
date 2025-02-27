package com.ssafy.shieldroneapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.*
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.lazy.AutoCenteringParams

@Composable
fun Layout(
    modifier: Modifier = Modifier,
    hasClock: Boolean = true,
    children: @Composable () -> Unit
) {
    // 스크롤 상태 관리
    val listState = rememberScalingLazyListState()
    
    Scaffold(
        timeText = {
            if (hasClock) {
                TimeText(
                    modifier = Modifier.scrollAway(listState)
                )
            }
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = listState
            )
        }
    ) {
        ScalingLazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {
            item {
                children()
            }
        }
    }
}