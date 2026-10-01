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
import com.bimiboo.toddlergames.core.model.ColorObjectItem
import com.bimiboo.toddlergames.core.model.GameColorType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ColorMatchingScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val totalLevels = 12

    val levelConfigs = remember {
        listOf(
            Pair(listOf(GameColorType.RED, GameColorType.BLUE), listOf(Pair(GameColorType.RED, "🍎"), Pair(GameColorType.BLUE, "🫐"))),
            Pair(listOf(GameColorType.YELLOW, GameColorType.GREEN), listOf(Pair(GameColorType.YELLOW, "🍌"), Pair(GameColorType.GREEN, "🥑"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.YELLOW), listOf(Pair(GameColorType.RED, "🚗"), Pair(GameColorType.BLUE, "🚙"), Pair(GameColorType.YELLOW, "🚕"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.GREEN), listOf(Pair(GameColorType.RED, "🎈"), Pair(GameColorType.BLUE, "🎈"), Pair(GameColorType.GREEN, "🎈"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.YELLOW, GameColorType.GREEN), listOf(Pair(GameColorType.RED, "🍓"), Pair(GameColorType.BLUE, "🫐"), Pair(GameColorType.YELLOW, "🍋"), Pair(GameColorType.GREEN, "🍏"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.YELLOW, GameColorType.GREEN), listOf(Pair(GameColorType.RED, "🦀"), Pair(GameColorType.BLUE, "🐳"), Pair(GameColorType.YELLOW, "🐥"), Pair(GameColorType.GREEN, "🐸"))),
            Pair(listOf(GameColorType.PURPLE, GameColorType.ORANGE, GameColorType.PINK), listOf(Pair(GameColorType.PURPLE, "🍬"), Pair(GameColorType.ORANGE, "🍊"), Pair(GameColorType.PINK, "🌸"))),
            Pair(listOf(GameColorType.RED, GameColorType.YELLOW, GameColorType.PINK, GameColorType.GREEN), listOf(Pair(GameColorType.RED, "🌹"), Pair(GameColorType.YELLOW, "🌻"), Pair(GameColorType.PINK, "🌺"), Pair(GameColorType.GREEN, "🍀"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.YELLOW, GameColorType.PURPLE), listOf(Pair(GameColorType.RED, "💎"), Pair(GameColorType.BLUE, "💎"), Pair(GameColorType.YELLOW, "⭐"), Pair(GameColorType.PURPLE, "🔮"))),
            Pair(listOf(GameColorType.RED, GameColorType.YELLOW, GameColorType.GREEN, GameColorType.PINK), listOf(Pair(GameColorType.RED, "🍧"), Pair(GameColorType.YELLOW, "🍦"), Pair(GameColorType.GREEN, "🍨"), Pair(GameColorType.PINK, "🍧"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.ORANGE, GameColorType.GREEN), listOf(Pair(GameColorType.RED, "🦞"), Pair(GameColorType.BLUE, "🐬"), Pair(GameColorType.ORANGE, "🐠"), Pair(GameColorType.GREEN, "🐢"))),
            Pair(listOf(GameColorType.RED, GameColorType.BLUE, GameColorType.YELLOW, GameColorType.GREEN), listOf(Pair(GameColorType.RED, "❤️"), Pair(GameColorType.BLUE, "💙"), Pair(GameColorType.YELLOW, "💛"), Pair(GameColorType.GREEN, "💚")))
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val (basketColors, initialItems) = levelConfigs[(currentLevel - 1) % levelConfigs.size]

    var items by remember {
        mutableStateOf(initialItems.shuffled().mapIndexed { idx, pair -> ColorObjectItem("${pair.first.name}_${idx}_$currentLevel", pair.first, pair.second) })
    }
    var errorItemId by remember { mutableStateOf<String?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        errorItemId = null
        currentLevel = level
        val config = levelConfigs[(level - 1) % levelConfigs.size]
        items = config.second.shuffled().mapIndexed { idx, pair -> ColorObjectItem("${pair.first.name}_${idx}_$level", pair.first, pair.second) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F9FF))
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
                        text = "🎨 Phân Loại Màu",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
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

            // Upper Color Baskets
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                basketColors.forEach { basket ->
                    val matchedCount = items.filter { it.colorType == basket && it.isMatched }.size

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (basketColors.size > 3) 60.dp else 72.dp, if (basketColors.size > 3) 76.dp else 88.dp)
                                .background(basket.color.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                .border(2.dp, basket.color, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🧺", fontSize = if (basketColors.size > 3) 24.sp else 30.sp)
                                if (matchedCount > 0) {
                                    val matchedItem = items.firstOrNull { it.colorType == basket && it.isMatched }
                                    Text(matchedItem?.emoji ?: "✓", fontSize = 16.sp)
                                }
                            }
                        }

                        Text(
                            text = basket.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = basket.color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Draggable Fruit Items
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(bottom = 36.dp)
            ) {
                items.forEach { item ->
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
                                .size(if (items.size > 3) 60.dp else 72.dp)
                                .shadow(8.dp, CircleShape)
                                .background(bgColor, CircleShape)
                                .border(3.dp, borderColor, CircleShape)
                                .pointerInput(item.id) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            if (offsetY < -130f) {
                                                soundManager.playSuccess()
                                                items = items.map {
                                                    if (it.id == item.id) it.copy(isMatched = true) else it
                                                }
                                                if (items.all { it.isMatched }) {
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
                            Text(text = item.emoji, fontSize = if (items.size > 3) 34.sp else 42.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(if (items.size > 3) 60.dp else 72.dp))
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
