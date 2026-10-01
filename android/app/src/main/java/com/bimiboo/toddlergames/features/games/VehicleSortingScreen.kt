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
import com.bimiboo.toddlergames.core.model.VehicleItem
import com.bimiboo.toddlergames.core.model.VehicleType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun VehicleSortingScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val totalLevels = 12

    val levelVehicles = remember {
        listOf(
            listOf(VehicleItem(1, "Ô tô", "🚗", VehicleType.LAND), VehicleItem(2, "Máy bay", "✈️", VehicleType.AIR)),
            listOf(VehicleItem(1, "Tàu thủy", "🚢", VehicleType.WATER), VehicleItem(2, "Tàu hỏa", "🚂", VehicleType.LAND)),
            listOf(VehicleItem(1, "Trực thăng", "🚁", VehicleType.AIR), VehicleItem(2, "Thuyền buồm", "⛵", VehicleType.WATER)),
            listOf(VehicleItem(1, "Xe buýt", "🚌", VehicleType.LAND), VehicleItem(2, "Tên lửa", "🚀", VehicleType.AIR), VehicleItem(3, "Tàu ngầm", "🤿", VehicleType.WATER)),
            listOf(VehicleItem(1, "Xe đạp", "🚲", VehicleType.LAND), VehicleItem(2, "Khinh khí cầu", "🎈", VehicleType.AIR), VehicleItem(3, "Cano", "🚤", VehicleType.WATER)),
            listOf(VehicleItem(1, "Xe cứu hỏa", "🚒", VehicleType.LAND), VehicleItem(2, "Đĩa bay", "🛸", VehicleType.AIR), VehicleItem(3, "Du thuyền", "🛥️", VehicleType.WATER)),
            listOf(VehicleItem(1, "Xe cảnh sát", "🚓", VehicleType.LAND), VehicleItem(2, "Phản lực", "🛩️", VehicleType.AIR), VehicleItem(3, "Phà biển", "⛴️", VehicleType.WATER)),
            listOf(VehicleItem(1, "Máy kéo", "🚜", VehicleType.LAND), VehicleItem(2, "Dù lượn", "🪂", VehicleType.AIR), VehicleItem(3, "Thuyền kayak", "🛶", VehicleType.WATER)),
            listOf(VehicleItem(1, "Xe cứu thương", "🚑", VehicleType.LAND), VehicleItem(2, "Drone", "🚁", VehicleType.AIR), VehicleItem(3, "Mô tô nước", "🚤", VehicleType.WATER)),
            listOf(VehicleItem(1, "Taxi", "🚕", VehicleType.LAND), VehicleItem(2, "Xe máy", "🛵", VehicleType.LAND), VehicleItem(3, "Xe tải", "🚚", VehicleType.LAND)),
            listOf(VehicleItem(1, "Máy bay", "✈️", VehicleType.AIR), VehicleItem(2, "Trực thăng", "🚁", VehicleType.AIR), VehicleItem(3, "Tên lửa", "🚀", VehicleType.AIR)),
            listOf(VehicleItem(1, "Xe đua", "🏎️", VehicleType.LAND), VehicleItem(2, "Máy bay", "✈️", VehicleType.AIR), VehicleItem(3, "Tàu chiến", "🛳️", VehicleType.WATER), VehicleItem(4, "Khinh khí cầu", "🎈", VehicleType.AIR))
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val currentItems = levelVehicles[(currentLevel - 1) % levelVehicles.size]

    val activeZones = remember(currentLevel) { currentItems.map { it.type }.distinct() }
    var vehicles by remember(currentLevel) { mutableStateOf(currentItems.shuffled()) }
    var errorVehicleId by remember { mutableStateOf<Int?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        errorVehicleId = null
        currentLevel = level
        val list = levelVehicles[(level - 1) % levelVehicles.size]
        vehicles = list.shuffled().map { it.copy(isMatched = false) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2FE))
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
                        text = "🚗 Phương Tiện Giao Thông",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF0369A1)
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

            // Upper Transport Stations / Zones
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                activeZones.forEach { zone ->
                    val matchedList = vehicles.filter { it.type == zone && it.isMatched }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (activeZones.size > 2) 74.dp else 90.dp, if (activeZones.size > 2) 80.dp else 95.dp)
                                .background(Color(0xFFF0F9FF), RoundedCornerShape(18.dp))
                                .border(2.dp, Color(0xFF38BDF8), RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = zone.category,
                                    fontSize = if (activeZones.size > 2) 28.sp else 36.sp
                                )
                                if (matchedList.isNotEmpty()) {
                                    Text("✓ x${matchedList.size}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                                }
                            }
                        }

                        Text(
                            text = zone.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF075985)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Draggable Vehicles
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(bottom = 36.dp)
            ) {
                vehicles.forEach { vehicle ->
                    if (!vehicle.isMatched) {
                        var offsetX by remember { mutableFloatStateOf(0f) }
                        var offsetY by remember { mutableFloatStateOf(0f) }
                        val isCurrentError = errorVehicleId == vehicle.id

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
                                .size(if (vehicles.size > 3) 62.dp else 74.dp)
                                .shadow(8.dp, CircleShape)
                                .background(bgColor, CircleShape)
                                .border(3.dp, borderColor, CircleShape)
                                .pointerInput(vehicle.id) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            if (offsetY < -130f) {
                                                soundManager.playSuccess()
                                                vehicles = vehicles.map {
                                                    if (it.id == vehicle.id) it.copy(isMatched = true) else it
                                                }
                                                if (vehicles.all { it.isMatched }) {
                                                    showCelebration = true
                                                }
                                            } else {
                                                // SAI: Báo đỏ, âm thanh lỗi, rung và bật về vị trí cũ để làm lại
                                                soundManager.playError()
                                                errorVehicleId = vehicle.id
                                                coroutineScope.launch {
                                                    delay(600)
                                                    if (errorVehicleId == vehicle.id) {
                                                        errorVehicleId = null
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
                            Text(text = vehicle.emoji, fontSize = if (vehicles.size > 3) 34.sp else 40.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(if (vehicles.size > 3) 62.dp else 74.dp))
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
