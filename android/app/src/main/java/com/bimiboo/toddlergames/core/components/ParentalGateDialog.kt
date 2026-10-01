package com.bimiboo.toddlergames.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bimiboo.toddlergames.core.audio.SoundManager
import kotlin.random.Random

@Composable
fun ParentalGateDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager.getInstance(context) }

    var num1 by remember { mutableStateOf(Random.nextInt(2, 9)) }
    var num2 by remember { mutableStateOf(Random.nextInt(2, 9)) }
    val correctAnswer = num1 * num2
    var inputAnswer by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Dành Cho Phụ Huynh",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Text(
                    text = "Vui lòng giải phép tính sau để tiếp tục:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "$num1  ×  $num2  =  ?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF8B5CF6)
                )

                Box(
                    modifier = Modifier
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (inputAnswer.isEmpty()) "___" else inputAnswer,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isError) {
                    Text(
                        text = "Chưa chính xác, thử lại nhé!",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                // Simple 3x4 keypad
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in 0..2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (col in 1..3) {
                                val digit = row * 3 + col
                                Button(
                                    onClick = {
                                        if (inputAnswer.length < 3) {
                                            inputAnswer += digit
                                            isError = false
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0))
                                ) {
                                    Text("$digit", color = Color(0xFF1E293B), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (inputAnswer.isNotEmpty()) inputAnswer = inputAnswer.dropLast(1)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE4E6))
                        ) {
                            Text("⌫", color = Color.Red, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                if (inputAnswer.length < 3) {
                                    inputAnswer += "0"
                                    isError = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0))
                        ) {
                            Text("0", color = Color(0xFF1E293B), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }

                        Button(
                            onClick = {
                                if (inputAnswer.toIntOrNull() == correctAnswer) {
                                    soundManager.playSuccess()
                                    onSuccess()
                                } else {
                                    soundManager.playError()
                                    isError = true
                                    inputAnswer = ""
                                    num1 = Random.nextInt(2, 9)
                                    num2 = Random.nextInt(2, 9)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("✓", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                TextButton(onClick = onDismiss) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        }
    }
}
