package com.bimiboo.toddlergames.features.games

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
import com.bimiboo.toddlergames.core.model.AnimalPuzzlePiece

@Composable
fun AnimalPuzzleScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val totalLevels = 12

    // 12 Levels with distinct animal groups
    val levelPuzzles = remember {
        listOf(
            // Level 1: Jungle
            listOf(AnimalPuzzlePiece(1, "Sư tử", "🦁"), AnimalPuzzlePiece(2, "Voi con", "🐘"), AnimalPuzzlePiece(3, "Hươu cao cổ", "🦒"), AnimalPuzzlePiece(4, "Khỉ con", "🐒")),
            // Level 2: Farm
            listOf(AnimalPuzzlePiece(1, "Bò sữa", "🐄"), AnimalPuzzlePiece(2, "Chú heo", "🐖"), AnimalPuzzlePiece(3, "Cừu con", "🐑"), AnimalPuzzlePiece(4, "Gà trống", "🐓")),
            // Level 3: Ocean
            listOf(AnimalPuzzlePiece(1, "Cá voi", "🐳"), AnimalPuzzlePiece(2, "Cá heo", "🐬"), AnimalPuzzlePiece(3, "Bạch tuộc", "🐙"), AnimalPuzzlePiece(4, "Cua biển", "🦀")),
            // Level 4: Forest
            listOf(AnimalPuzzlePiece(1, "Gấu nâu", "🐻"), AnimalPuzzlePiece(2, "Cáo đỏ", "🦊"), AnimalPuzzlePiece(3, "Thỏ trắng", "🐇"), AnimalPuzzlePiece(4, "Sóc con", "🐿️")),
            // Level 5: Pets
            listOf(AnimalPuzzlePiece(1, "Cún con", "🐶"), AnimalPuzzlePiece(2, "Mèo con", "🐱"), AnimalPuzzlePiece(3, "Chuột hamster", "🐹"), AnimalPuzzlePiece(4, "Vẹt xanh", "🦜")),
            // Level 6: Birds
            listOf(AnimalPuzzlePiece(1, "Cú mèo", "🦉"), AnimalPuzzlePiece(2, "Hồng hạc", "🦩"), AnimalPuzzlePiece(3, "Đại bàng", "🦅"), AnimalPuzzlePiece(4, "Vịt con", "🦆")),
            // Level 7: Insects
            listOf(AnimalPuzzlePiece(1, "Bướm hoa", "🦋"), AnimalPuzzlePiece(2, "Ong mật", "🐝"), AnimalPuzzlePiece(3, "Bọ rùa", "🐞"), AnimalPuzzlePiece(4, "Ốc sên", "🐌")),
            // Level 8: Polar Cold
            listOf(AnimalPuzzlePiece(1, "Chim cánh cụt", "🐧"), AnimalPuzzlePiece(2, "Gấu bắc cực", "🐻‍❄️"), AnimalPuzzlePiece(3, "Hải cẩu", "🦭"), AnimalPuzzlePiece(4, "Tuần lộc", "🦌")),
            // Level 9: Safari
            listOf(AnimalPuzzlePiece(1, "Ngựa vằn", "🦓"), AnimalPuzzlePiece(2, "Hà mã", "🦛"), AnimalPuzzlePiece(3, "Tê giác", "🦏"), AnimalPuzzlePiece(4, "Báo gấm", "🐆")),
            // Level 10: Dinosaurs
            listOf(AnimalPuzzlePiece(1, "Khủng long T-Rex", "🦖"), AnimalPuzzlePiece(2, "Khủng long cổ dài", "🦕"), AnimalPuzzlePiece(3, "Rồng lửa", "🐉"), AnimalPuzzlePiece(4, "Trứng khủng long", "🥚")),
            // Level 11: Cute Reptiles & Amphibians
            listOf(AnimalPuzzlePiece(1, "Ếch xanh", "🐸"), AnimalPuzzlePiece(2, "Rùa biển", "🐢"), AnimalPuzzlePiece(3, "Tắc kè", "🦎"), AnimalPuzzlePiece(4, "Cá sấu", "🐊")),
            // Level 12: Fantasy & Magic
            listOf(AnimalPuzzlePiece(1, "Kỳ lân", "🦄"), AnimalPuzzlePiece(2, "Gấu trúc", "🐼"), AnimalPuzzlePiece(3, "Koala", "🐨"), AnimalPuzzlePiece(4, "Hổ con", "🐯"))
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val currentAnimalList = levelPuzzles[(currentLevel - 1) % levelPuzzles.size]

    var pieces by remember(currentLevel) { mutableStateOf(currentAnimalList.shuffled()) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        currentLevel = level
        val list = levelPuzzles[(level - 1) % levelPuzzles.size]
        pieces = list.shuffled().map { it.copy(isPlaced = false) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB))
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
                        text = "🧩 Ghép Đôi Động Vật",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF78350F)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
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

            // 2x2 Silhouette Board
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(26.dp))
                    .padding(16.dp)
            ) {
                for (row in 0..1) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        for (col in 0..1) {
                            val animal = currentAnimalList[row * 2 + col]
                            val isPlaced = pieces.firstOrNull { it.id == animal.id }?.isPlaced == true

                            Box(
                                modifier = Modifier
                                    .size(115.dp, 95.dp)
                                    .background(
                                        if (isPlaced) Color(0xFFFEF3C7) else Color(0xFFF1F5F9),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        2.dp,
                                        if (isPlaced) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    if (isPlaced) {
                                        Text(text = animal.emoji, fontSize = 40.sp)
                                    } else {
                                        Text(text = "❓", fontSize = 28.sp)
                                    }
                                    Text(
                                        text = animal.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPlaced) Color(0xFFD97706) else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Available Pieces to Match
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 36.dp)
            ) {
                pieces.forEach { piece ->
                    if (!piece.isPlaced) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .shadow(6.dp, CircleShape)
                                .background(Color.White, CircleShape)
                                .clickable {
                                    soundManager.playSuccess()
                                    pieces = pieces.map {
                                        if (it.id == piece.id) it.copy(isPlaced = true) else it
                                    }
                                    if (pieces.all { it.isPlaced }) {
                                        showCelebration = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = piece.emoji, fontSize = 36.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(64.dp))
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
