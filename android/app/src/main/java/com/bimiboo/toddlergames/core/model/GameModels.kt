package com.bimiboo.toddlergames.core.model

import androidx.compose.ui.graphics.Color
import com.bimiboo.toddlergames.ui.theme.*

enum class GameCategory(
    val route: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val themeColor: Color,
    val totalLevels: Int = 12
) {
    SHAPES("shapes", "Hình Khối", "Shapes (12 màn)", "🟡", PastelPink, 12),
    COLORS("colors", "Màu Sắc", "Colors (12 màn)", "🎨", PastelBlue, 12),
    SIZES("sizes", "Kích Thước", "Sizes (12 màn)", "📏", PastelGreen, 12),
    PUZZLE("puzzle", "Ghép Hình", "Puzzles (12 màn)", "🧩", PastelOrange, 12),
    COUNTING("counting", "Tập Đếm", "Counting (12 màn)", "🎈", PastelPurple, 12),
    FEED_ANIMAL("feed_animal", "Bé Cho Ăn", "Feed Animals (12 màn)", "🍖", Color(0xFFFDE047), 12),
    VEHICLES("vehicles", "Phương Tiện", "Vehicles (12 màn)", "🚗", Color(0xFF67E8F9), 12),
    ODD_ONE_OUT("odd_one_out", "Tìm Khác Biệt", "Odd One Out (12 màn)", "🔍", Color(0xFFF472B6), 12)
}

// MARK: - Shapes
enum class ShapeType(val label: String, val color: Color, val symbol: String, val iconUnicode: String) {
    CIRCLE("Hình tròn", Color(0xFFEF4444), "○", "🔴"),
    SQUARE("Hình vuông", Color(0xFF3B82F6), "□", "🟦"),
    TRIANGLE("Tam giác", Color(0xFF10B981), "△", "🔺"),
    STAR("Ngôi sao", Color(0xFFF59E0B), "☆", "⭐"),
    HEART("Trái tim", Color(0xFFEC4899), "♡", "💖"),
    DIAMOND("Hình thoi", Color(0xFF8B5CF6), "◇", "💎"),
    MOON("Mặt trăng", Color(0xFFFCD34D), "☽", "🌙"),
    CLOUD("Đám mây", Color(0xFF38BDF8), "☁", "☁️")
}

data class ShapeGameItem(
    val id: String,
    val type: ShapeType,
    var isMatched: Boolean = false
)

// MARK: - Colors
enum class GameColorType(val label: String, val color: Color) {
    RED("Đỏ", Color(0xFFEF4444)),
    BLUE("Xanh dương", Color(0xFF3B82F6)),
    YELLOW("Vàng", Color(0xFFF59E0B)),
    GREEN("Xanh lá", Color(0xFF10B981)),
    PURPLE("Tím", Color(0xFF8B5CF6)),
    ORANGE("Cam", Color(0xFFF97316)),
    PINK("Hồng", Color(0xFFEC4899))
}

data class ColorObjectItem(
    val id: String,
    val colorType: GameColorType,
    val emoji: String,
    var isMatched: Boolean = false
)

// MARK: - Sizes
data class SizeItem(
    val id: String,
    val rank: Int, // 1: small, 2: medium, 3: large, 4: extra large
    val label: String,
    val emoji: String,
    var isMatched: Boolean = false
)

// MARK: - Animal Puzzle Piece
data class AnimalPuzzlePiece(
    val id: Int,
    val name: String,
    val emoji: String,
    var isPlaced: Boolean = false
)

// MARK: - Feed Animals
data class AnimalFoodPair(
    val id: Int,
    val animalName: String,
    val animalEmoji: String,
    val foodName: String,
    val foodEmoji: String,
    var isFed: Boolean = false
)

// MARK: - Vehicles
enum class VehicleType(val label: String, val category: String) {
    LAND("Đường bộ", "🚗"),
    AIR("Đường hàng không", "✈️"),
    WATER("Đường thủy", "🚢")
}

data class VehicleItem(
    val id: Int,
    val name: String,
    val emoji: String,
    val type: VehicleType,
    var isMatched: Boolean = false
)

// MARK: - Odd One Out
data class OddChoiceItem(
    val id: Int,
    val emoji: String,
    val name: String,
    val isOdd: Boolean
)
