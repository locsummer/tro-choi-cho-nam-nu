package com.bimiboo.toddlergames.features.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bimiboo.toddlergames.core.audio.SoundManager
import com.bimiboo.toddlergames.core.components.GameCelebrationDialog
import com.bimiboo.toddlergames.core.model.SizeItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SizeOrderingScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val totalLevels = 12

    val levelEmojis = remember {
        listOf("🐻", "🐟", "🎂", "🌲", "🚗", "🏠", "⚽", "⛄", "🍎", "⭐", "🚀", "🐧")
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val currentEmoji = levelEmojis[(currentLevel - 1) % levelEmojis.size]

    val sizeItemsInitial = remember(currentLevel) {
        listOf(
            SizeItem("1_$currentLevel", 1, "Nhỏ", currentEmoji),
            SizeItem("2_$currentLevel", 2, "Vừa", currentEmoji),
            SizeItem("3_$currentLevel", 3, "Lớn", currentEmoji)
        )
    }

    var availableItems by remember(currentLevel) { mutableStateOf(sizeItemsInitial.shuffled()) }
    var placedSlots by remember(currentLevel) { mutableStateOf(listOf<SizeItem?>(null, null, null)) }
    var errorItemId by remember { mutableStateOf<String?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        errorItemId = null
        currentLevel = level
        placedSlots = listOf(null, null, null)
        val emoji = levelEmojis[(level - 1) % levelEmojis.size]
        availableItems = listOf(
            SizeItem("1_$level", 1, "Nhỏ", emoji),
            SizeItem("2_$level", 2, "Vừa", emoji),
            SizeItem("3_$level", 3, "Lớn", emoji)
        ).shuffled()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0FDF4))
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
                        text = "📏 Xếp Kích Thước",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16A34A)
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

            // Upper Target Slots
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                for (i in 0..2) {
                    val slotRank = i + 1
                    val placed = placedSlots[i]
                    val boxWidth = (65 + i * 18).dp
                    val boxHeight = (75 + i * 22).dp

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(boxWidth, boxHeight)
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                                .border(2.dp, Color(0xFF86EFAC), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (placed != null) {
                                Text(
                                    text = currentEmoji,
                                    fontSize = (26 + i * 14).sp
                                )
                            } else {
                                Text(
                                    text = "$slotRank",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )
                            }
                        }

                        Text(
                            text = when (slotRank) {
                                1 -> "Nhỏ"
                                2 -> "Vừa"
                                else -> "Lớn"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Items to tap / place
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 36.dp)
            ) {
                // Next expected rank (1, 2, or 3)
                val expectedRank = (placedSlots.indexOfFirst { it == null } + 1).takeIf { it > 0 } ?: 1

                availableItems.forEach { item ->
                    if (!item.isMatched) {
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
                                .size(75.dp)
                                .shadow(6.dp, CircleShape)
                                .background(bgColor, CircleShape)
                                .border(3.dp, borderColor, CircleShape)
                                .clickable {
                                    if (item.rank == expectedRank) {
                                        soundManager.playSuccess()
                                        val index = item.rank - 1
                                        val newSlots = placedSlots.toMutableList()
                                        newSlots[index] = item
                                        placedSlots = newSlots

                                        availableItems = availableItems.map {
                                            if (it.id == item.id) it.copy(isMatched = true) else it
                                        }

                                        if (placedSlots.all { it != null }) {
                                            showCelebration = true
                                        }
                                    } else {
                                        // SAI: Bấm sai thứ tự kích thước -> Báo đỏ, kêu âm thanh sai
                                        soundManager.playError()
                                        errorItemId = item.id
                                        coroutineScope.launch {
                                            delay(600)
                                            if (errorItemId == item.id) {
                                                errorItemId = null
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.emoji,
                                fontSize = (26 + (item.rank - 1) * 12).sp
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(75.dp))
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
