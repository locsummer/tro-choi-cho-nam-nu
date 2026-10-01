package com.bimiboo.toddlergames.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bimiboo.toddlergames.core.audio.SoundManager
import com.bimiboo.toddlergames.core.components.ParentalGateDialog
import com.bimiboo.toddlergames.core.model.GameCategory

@Composable
fun HomeScreen(
    onNavigateToGame: (GameCategory) -> Unit
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }

    var isMuted by remember { mutableStateOf(soundManager.isMuted()) }
    var showParentalGate by remember { mutableStateOf(false) }
    var showParentalSettings by remember { mutableStateOf(false) }

    val categories = remember { GameCategory.entries }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFFBEB), Color(0xFFF0FDF4), Color(0xFFEFF6FF))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        isMuted = soundManager.toggleMute()
                        soundManager.playPop()
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color(0xFFF97316), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Audio Toggle",
                        tint = Color.White
                    )
                }

                Button(
                    onClick = {
                        soundManager.playPop()
                        showParentalGate = true
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Phụ huynh", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // App Header & Mascot
            Text("🐻 ✨ 🎈", fontSize = 32.sp)

            Text(
                text = "Trò Chơi Cho Bé",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1E293B)
            )

            Text(
                text = "8 Chủ Đề • Mỗi Chủ Đề 12 Màn Chơi",
                fontSize = 12.sp,
                color = Color(0xFF0284C7),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Game Cards Grid (8 categories)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { game ->
                    GameCardItem(
                        game = game,
                        onClick = {
                            soundManager.playPop()
                            onNavigateToGame(game)
                        }
                    )
                }
            }
        }

        if (showParentalGate) {
            ParentalGateDialog(
                onDismiss = { showParentalGate = false },
                onSuccess = {
                    showParentalGate = false
                    showParentalSettings = true
                }
            )
        }

        if (showParentalSettings) {
            AlertDialog(
                onDismissRequest = { showParentalSettings = false },
                title = { Text("Cài Đặt Dành Cho Phụ Huynh", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("• Phiên bản: 1.2.0")
                        Text("• Tổng số trò chơi: 8 chủ đề (96 màn chơi)")
                        Text("• Độ tuổi: 2 - 5 tuổi (Trẻ mầm non)")
                        Text("• Chế độ: Tự động chuyển màn tiếp theo khi hoàn thành.")
                    }
                },
                confirmButton = {
                    Button(onClick = { showParentalSettings = false }) {
                        Text("Đóng")
                    }
                }
            )
        }
    }
}

@Composable
fun GameCardItem(
    game: GameCategory,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(game.themeColor.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = game.emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = game.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF1E293B)
            )

            Text(
                text = game.subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }
    }
}
