package com.bimiboo.toddlergames.features.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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

data class CountingGameItem(
    val id: Int,
    val emoji: String,
    var isCounted: Boolean = false
)

@Composable
fun CountingScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val totalLevels = 12

    // 12 Levels with count quantities & item emoji
    val levelConfigs = remember {
        listOf(
            Pair(3, "🎈"), // 1: 3 Balloons
            Pair(3, "🍎"), // 2: 3 Apples
            Pair(4, "⭐"), // 3: 4 Stars
            Pair(4, "🐥"), // 4: 4 Ducks
            Pair(5, "🍓"), // 5: 5 Strawberries
            Pair(5, "🚗"), // 6: 5 Cars
            Pair(6, "🍬"), // 7: 6 Candies
            Pair(6, "🐟"), // 8: 6 Fishes
            Pair(7, "🌸"), // 9: 7 Flowers
            Pair(8, "🍪"), // 10: 8 Cookies
            Pair(9, "💎"), // 11: 9 Gems
            Pair(10, "🦋") // 12: 10 Butterflies
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val (targetCount, currentEmoji) = levelConfigs[(currentLevel - 1) % levelConfigs.size]

    var items by remember(currentLevel) {
        mutableStateOf((1..targetCount).map { CountingGameItem(it, currentEmoji) })
    }
    var countedCount by remember(currentLevel) { mutableIntStateOf(0) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        currentLevel = level
        countedCount = 0
        val (cnt, emoji) = levelConfigs[(level - 1) % levelConfigs.size]
        items = (1..cnt).map { CountingGameItem(it, emoji) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5FF))
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
                        text = "🎈 Bé Học Đếm",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF581C87)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9333EA)
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

            Spacer(modifier = Modifier.height(14.dp))

            // Score Counter
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = "Đã đếm: $countedCount / $targetCount",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Grid of Items to Count
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (targetCount <= 4) 2 else if (targetCount <= 6) 3 else 4),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items) { item ->
                    if (!item.isCounted) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Color.White, CircleShape)
                                .clickable {
                                    soundManager.playPop()
                                    countedCount++
                                    items = items.map {
                                        if (it.id == item.id) it.copy(isCounted = true) else it
                                    }
                                    if (countedCount >= targetCount) {
                                        showCelebration = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.emoji, fontSize = 38.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color(0xFFF3E8FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✨", fontSize = 28.sp)
                        }
                    }
                }
            }

            Text(
                text = "Chạm vào từng hình để đếm nhé! 👇",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 20.dp)
            )
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
