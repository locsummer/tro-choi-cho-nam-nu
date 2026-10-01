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
import com.bimiboo.toddlergames.core.model.AnimalFoodPair
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FeedAnimalScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    val totalLevels = 12

    val levelPairs = remember {
        listOf(
            listOf(AnimalFoodPair(1, "Khỉ", "🐒", "Chuối", "🍌"), AnimalFoodPair(2, "Thỏ", "🐇", "Cà rốt", "🥕")),
            listOf(AnimalFoodPair(1, "Mèo", "🐱", "Cá", "🐟"), AnimalFoodPair(2, "Cún", "🐶", "Xương", "🍖")),
            listOf(AnimalFoodPair(1, "Gấu trúc", "🐼", "Tre trúc", "🎋"), AnimalFoodPair(2, "Chuột", "🐭", "Phô mai", "🧀")),
            listOf(AnimalFoodPair(1, "Bò sữa", "🐄", "Bó cỏ", "🌾"), AnimalFoodPair(2, "Gấu nâu", "🐻", "Mật ong", "🍯")),
            listOf(AnimalFoodPair(1, "Chim non", "🐦", "Sâu bọ", "🐛"), AnimalFoodPair(2, "Voi con", "🐘", "Quả táo", "🍎")),
            listOf(AnimalFoodPair(1, "Khỉ", "🐒", "Chuối", "🍌"), AnimalFoodPair(2, "Thỏ", "🐇", "Cà rốt", "🥕"), AnimalFoodPair(3, "Mèo", "🐱", "Cá", "🐟")),
            listOf(AnimalFoodPair(1, "Cún", "🐶", "Xương", "🍖"), AnimalFoodPair(2, "Gấu trúc", "🐼", "Tre", "🎋"), AnimalFoodPair(3, "Chuột", "🐭", "Phô mai", "🧀")),
            listOf(AnimalFoodPair(1, "Chú ngựa", "🐎", "Cà rốt", "🥕"), AnimalFoodPair(2, "Sóc con", "🐿️", "Hạt dẻ", "🌰"), AnimalFoodPair(3, "Bướm", "🦋", "Mật hoa", "🌸")),
            listOf(AnimalFoodPair(1, "Ếch cốm", "🐸", "Ruồi", "🪰"), AnimalFoodPair(2, "Vịt con", "🦆", "Bánh mì", "🍞"), AnimalFoodPair(3, "Hải cẩu", "🦭", "Cá", "🐟")),
            listOf(AnimalFoodPair(1, "Khỉ", "🐒", "Chuối", "🍌"), AnimalFoodPair(2, "Mèo", "🐱", "Cá", "🐟"), AnimalFoodPair(3, "Cún", "🐶", "Xương", "🍖"), AnimalFoodPair(4, "Thỏ", "🐇", "Cà rốt", "🥕")),
            listOf(AnimalFoodPair(1, "Gấu", "🐻", "Mật ong", "🍯"), AnimalFoodPair(2, "Chuột", "🐭", "Phô mai", "🧀"), AnimalFoodPair(3, "Bò", "🐄", "Cỏ", "🌾"), AnimalFoodPair(4, "Chim", "🐦", "Sâu", "🐛")),
            listOf(AnimalFoodPair(1, "Sư tử", "🦁", "Thịt đùi", "🥩"), AnimalFoodPair(2, "Voi", "🐘", "Dưa hấu", "🍉"), AnimalFoodPair(3, "Gấu trúc", "🐼", "Trúc", "🎋"), AnimalFoodPair(4, "Khỉ", "🐒", "Chuối", "🍌"))
        )
    }

    var currentLevel by remember { mutableIntStateOf(1) }
    val currentPairList = levelPairs[(currentLevel - 1) % levelPairs.size]

    var animals by remember(currentLevel) { mutableStateOf(currentPairList.map { it.copy(isFed = false) }) }
    var foods by remember(currentLevel) { mutableStateOf(currentPairList.shuffled().map { it.copy(isFed = false) }) }
    var errorFoodId by remember { mutableStateOf<Int?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    fun loadLevel(level: Int) {
        showCelebration = false
        errorFoodId = null
        currentLevel = level
        val pairs = levelPairs[(level - 1) % levelPairs.size]
        animals = pairs.map { it.copy(isFed = false) }
        foods = pairs.shuffled().map { it.copy(isFed = false) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFEFCE8))
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
                        text = "🍖 Bé Cho Thú Ăn",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color(0xFF854D0E)
                    )
                    Text(
                        text = "Màn $currentLevel / $totalLevels",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFCA8A04)
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

            // Upper Animal Targets
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                animals.forEach { animal ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (animals.size > 3) 62.dp else 78.dp)
                                .background(if (animal.isFed) Color(0xFFDCFCE7) else Color(0xFFFEF3C7), RoundedCornerShape(18.dp))
                                .border(2.dp, if (animal.isFed) Color(0xFF22C55E) else Color(0xFFF59E0B), RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (animal.isFed) "😋" else animal.animalEmoji,
                                    fontSize = if (animals.size > 3) 28.sp else 36.sp
                                )
                                if (animal.isFed) {
                                    Text(animal.foodEmoji, fontSize = 14.sp)
                                }
                            }
                        }

                        Text(
                            text = animal.animalName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF713F12)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Draggable Food Items
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(bottom = 36.dp)
            ) {
                foods.forEach { food ->
                    if (!food.isFed) {
                        var offsetX by remember { mutableFloatStateOf(0f) }
                        var offsetY by remember { mutableFloatStateOf(0f) }
                        val isCurrentError = errorFoodId == food.id

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
                                .size(if (foods.size > 3) 62.dp else 76.dp)
                                .shadow(8.dp, CircleShape)
                                .background(bgColor, CircleShape)
                                .border(3.dp, borderColor, CircleShape)
                                .pointerInput(food.id) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            if (offsetY < -130f) {
                                                soundManager.playSuccess()
                                                animals = animals.map {
                                                    if (it.id == food.id) it.copy(isFed = true) else it
                                                }
                                                foods = foods.map {
                                                    if (it.id == food.id) it.copy(isFed = true) else it
                                                }
                                                if (animals.all { it.isFed }) {
                                                    showCelebration = true
                                                }
                                            } else {
                                                // SAI: Báo đỏ, âm thanh lỗi, rung và bật về vị trí cũ để làm lại
                                                soundManager.playError()
                                                errorFoodId = food.id
                                                coroutineScope.launch {
                                                    delay(600)
                                                    if (errorFoodId == food.id) {
                                                        errorFoodId = null
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
                            Text(text = food.foodEmoji, fontSize = if (foods.size > 3) 34.sp else 42.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.size(if (foods.size > 3) 62.dp else 76.dp))
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
