package com.bimiboo.toddlergames.features.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bimiboo.toddlergames.core.audio.SoundManager
import com.bimiboo.toddlergames.core.components.GameCelebrationDialog
import com.bimiboo.toddlergames.core.model.ShapeGameItem
import com.bimiboo.toddlergames.core.model.ShapeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ShapeSortingScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val totalLevels = 12

    val levelShapeSets = remember {
        listOf(
            listOf(ShapeType.CIRCLE, ShapeType.SQUARE),
            listOf(ShapeType.TRIANGLE, ShapeType.STAR),
            listOf(ShapeType.CIRCLE, ShapeType.TRIANGLE, ShapeType.SQUARE),
            listOf(ShapeType.HEART, ShapeType.STAR, ShapeType.CIRCLE),
            listOf(ShapeType.CIRCLE, ShapeType.SQUARE, ShapeType.TRIANGLE, ShapeType.STAR),
            listOf(ShapeType.HEART, ShapeType.DIAMOND, ShapeType.STAR, ShapeType.MOON),
            listOf(ShapeType.CIRCLE, ShapeType.CLOUD, ShapeType.HEART, ShapeType.STAR),
            listOf(ShapeType.SQUARE, ShapeType.DIAMOND, ShapeType.TRIANGLE, ShapeType.MOON),
            listOf(ShapeType.CLOUD, ShapeType.STAR, ShapeType.HEART, ShapeType.CIRCLE),
            listOf(ShapeType.DIAMOND, ShapeType.SQUARE, ShapeType.CLOUD, ShapeType.TRIANGLE),
            listOf(ShapeType.MOON, ShapeType.STAR, ShapeType.CLOUD, ShapeType.HEART),
            listOf(ShapeType.CIRCLE, ShapeType.SQUARE, ShapeType.TRIANGLE, ShapeType.STAR, ShapeType.HEART)
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val currentTargets = levelShapeSets[(currentLevel - 1) % levelShapeSets.size]

    var availableItems by remember {
        mutableStateOf(currentTargets.shuffled().map { ShapeGameItem(it.name + "_${currentLevel}", it) })
    }
    var errorItemId by remember { mutableStateOf<String?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        errorItemId = null
        currentLevel = level
        val targets = levelShapeSets[(level - 1) % levelShapeSets.size]
        availableItems = targets.shuffled().map { ShapeGameItem(it.name + "_${level}", it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF4F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        soundManager.playPop()
                        onBack()
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color(0xFFF97316), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🟡 Hình Khối",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF334155)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE11D48)
                    )
                }

                IconButton(
                    onClick = {
                        soundManager.playPop()
                        loadLevel(currentLevel)
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color(0xFF3B82F6), CircleShape)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Upper Target Silhouettes Area
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                currentTargets.forEach { shape ->
                    val isMatched = availableItems.firstOrNull { it.type == shape }?.isMatched == true

                    Box(
                        modifier = Modifier
                            .size(if (currentTargets.size > 4) 58.dp else 68.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isMatched) shape.color.copy(alpha = 0.2f) else Color(0xFFF1F5F9))
                            .border(
                                width = 2.dp,
                                color = if (isMatched) shape.color else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isMatched) {
                            Text(text = shape.iconUnicode, fontSize = if (currentTargets.size > 4) 28.sp else 34.sp)
                        } else {
                            Text(
                                text = shape.symbol,
                                fontSize = if (currentTargets.size > 4) 26.sp else 30.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Draggable Items
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                availableItems.forEach { item ->
                    if (!item.isMatched) {
                        var offsetX by remember { mutableFloatStateOf(0f) }
                        var offsetY by remember { mutableFloatStateOf(0f) }
                        val isCurrentError = errorItemId == item.id

                        val bgColor by animateColorAsState(
                            targetValue = if (isCurrentError) Color(0xFFFFE4E6) else Color.White,
                            label = "bgColor"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isCurrentError) Color.Red else Color.Transparent,
                            label = "borderColor"
                        )

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .size(if (currentTargets.size > 4) 58.dp else 68.dp)
                                .shadow(8.dp, CircleShape)
                                .background(bgColor, CircleShape)
                                .border(3.dp, borderColor, CircleShape)
                                .pointerInput(item.id) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            if (offsetY < -130f) {
                                                soundManager.playSuccess()
                                                availableItems = availableItems.map {
                                                    if (it.id == item.id) it.copy(isMatched = true) else it
                                                }
                                                if (availableItems.all { it.isMatched }) {
                                                    showCelebration = true
                                                }
                                            } else {
                                                // SAI: Báo đỏ, âm thanh lỗi, rung và bật về vị trí cũ để làm lại
                                                soundManager.playError()
                                                errorItemId = item.id
                                                coroutineScope.launch {
                                                    delay(600)
                                                    if (errorItemId == item.id) {
                                                        errorItemId = null
                                                    }
                                                }
                                            }
                                            offsetX = 0f
                                            offsetY = 0f
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            offsetX += dragAmount.x
                                            offsetY += dragAmount.y
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.type.iconUnicode, fontSize = if (currentTargets.size > 4) 32.sp else 38.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(if (currentTargets.size > 4) 58.dp else 68.dp))
                    }
                }
            }
        }

        if (showCelebration) {
            GameCelebrationDialog(
                currentLevel = currentLevel,
                totalLevels = totalLevels,
                onReplay = { loadLevel(currentLevel) },
                onNextLevel = {
                    if (currentLevel < totalLevels) {
                        loadLevel(currentLevel + 1)
                    } else {
                        loadLevel(1)
                    }
                },
                onMenu = onBack
            )
        }
    }
}
