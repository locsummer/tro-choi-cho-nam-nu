package com.bimiboo.toddlergames.features.games

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bimiboo.toddlergames.core.audio.SoundManager
import com.bimiboo.toddlergames.core.components.GameCelebrationDialog
import com.bimiboo.toddlergames.core.model.OddChoiceItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OddOneOutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val totalLevels = 12

    val levelQuestions = remember {
        listOf(
            listOf(OddChoiceItem(1, "🍎", "Táo", false), OddChoiceItem(2, "🍎", "Táo", false), OddChoiceItem(3, "🍎", "Táo", false), OddChoiceItem(4, "🚗", "Ô tô", true)),
            listOf(OddChoiceItem(1, "🐦", "Chim", false), OddChoiceItem(2, "🐟", "Cá", true), OddChoiceItem(3, "🐦", "Chim", false), OddChoiceItem(4, "🐦", "Chim", false)),
            listOf(OddChoiceItem(1, "🔴", "Đỏ", false), OddChoiceItem(2, "🔴", "Đỏ", false), OddChoiceItem(3, "🟡", "Vàng", true), OddChoiceItem(4, "🔴", "Đỏ", false)),
            listOf(OddChoiceItem(1, "🐱", "Mèo", false), OddChoiceItem(2, "🐱", "Mèo", false), OddChoiceItem(3, "🍌", "Chuối", true), OddChoiceItem(4, "🐱", "Mèo", false)),
            listOf(OddChoiceItem(1, "🟦", "Vuông", false), OddChoiceItem(2, "⭐", "Sao", true), OddChoiceItem(3, "🟦", "Vuông", false), OddChoiceItem(4, "🟦", "Vuông", false)),
            listOf(OddChoiceItem(1, "✈️", "Máy bay", false), OddChoiceItem(2, "✈️", "Máy bay", false), OddChoiceItem(3, "✈️", "Máy bay", false), OddChoiceItem(4, "🚢", "Tàu thủy", true)),
            listOf(OddChoiceItem(1, "🐶", "Chó", false), OddChoiceItem(2, "🍦", "Kem", true), OddChoiceItem(3, "🐶", "Chó", false), OddChoiceItem(4, "🐶", "Chó", false)),
            listOf(OddChoiceItem(1, "🌸", "Hoa", false), OddChoiceItem(2, "🌸", "Hoa", false), OddChoiceItem(3, "🐸", "Ếch", true), OddChoiceItem(4, "🌸", "Hoa", false)),
            listOf(OddChoiceItem(1, "🎈", "Bóng bay", false), OddChoiceItem(2, "🎈", "Bóng bay", false), OddChoiceItem(3, "🍕", "Pizza", true), OddChoiceItem(4, "🎈", "Bóng bay", false)),
            listOf(OddChoiceItem(1, "🎩", "Mũ", false), OddChoiceItem(2, "🌲", "Cây", true), OddChoiceItem(3, "🎩", "Mũ", false), OddChoiceItem(4, "🎩", "Mũ", false)),
            listOf(OddChoiceItem(1, "🍬", "Kẹo", false), OddChoiceItem(2, "🔑", "Chìa khóa", true), OddChoiceItem(3, "🍬", "Kẹo", false), OddChoiceItem(4, "🍬", "Kẹo", false)),
            listOf(OddChoiceItem(1, "⭐", "Sao", false), OddChoiceItem(2, "⭐", "Sao", false), OddChoiceItem(3, "⭐", "Sao", false), OddChoiceItem(4, "💖", "Tim", true))
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    var currentChoices by remember(currentLevel) {
        mutableStateOf(levelQuestions[(currentLevel - 1) % levelQuestions.size].shuffled())
    }
    var errorItemId by remember { mutableStateOf<Int?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        errorItemId = null
        currentLevel = level
        currentChoices = levelQuestions[(level - 1) % levelQuestions.size].shuffled()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF2F8))
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
                        text = "🔍 Tìm Hình Khác Biệt",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF9D174D)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDB2777)
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

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Hình nào khác với các hình còn lại? 🤔",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF831843),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2x2 Grid of Choices
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(currentChoices) { item ->
                    val isCurrentError = errorItemId == item.id

                    val cardColor by animateColorAsState(
                        targetValue = if (isCurrentError) Color(0xFFFFE4E6) else Color.White,
                        label = "cardColor"
                    )
                    val borderColor by animateColorAsState(
                        targetValue = if (isCurrentError) Color.Red else Color.Transparent,
                        label = "borderColor"
                    )

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .border(3.dp, borderColor, RoundedCornerShape(24.dp))
                            .clickable {
                                if (item.isOdd) {
                                    soundManager.playSuccess()
                                    showCelebration = true
                                } else {
                                    // SAI: Bấm vào hình thường -> Báo đỏ, âm thanh lỗi, rung và cho làm lại
                                    soundManager.playError()
                                    errorItemId = item.id
                                    coroutineScope.launch {
                                        delay(600)
                                        if (errorItemId == item.id) {
                                            errorItemId = null
                                        }
                                    }
                                }
                            }
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = item.emoji, fontSize = 52.sp)
                        }
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
