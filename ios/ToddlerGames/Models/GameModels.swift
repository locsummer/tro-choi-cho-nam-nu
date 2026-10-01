import SwiftUI

// MARK: - 8 Game Categories
enum GameType: String, CaseIterable, Identifiable {
    case shapes = "shapes"
    case colors = "colors"
    case sizes = "sizes"
    case puzzle = "puzzle"
    case counting = "counting"
    case feedAnimal = "feed_animal"
    case vehicles = "vehicles"
    case oddOneOut = "odd_one_out"
    
    var id: String { rawValue }
    
    var title: String {
        switch self {
        case .shapes: return "Hình Khối"
        case .colors: return "Màu Sắc"
        case .sizes: return "Kích Thước"
        case .puzzle: return "Ghép Hình"
        case .counting: return "Tập Đếm"
        case .feedAnimal: return "Bé Cho Ăn"
        case .vehicles: return "Phương Tiện"
        case .oddOneOut: return "Tìm Khác Biệt"
        }
    }
    
    var englishTitle: String {
        switch self {
        case .shapes: return "Shapes (12 màn)"
        case .colors: return "Colors (12 màn)"
        case .sizes: return "Sizes (12 màn)"
        case .puzzle: return "Puzzles (12 màn)"
        case .counting: return "Counting (12 màn)"
        case .feedAnimal: return "Feed Pets (12 màn)"
        case .vehicles: return "Vehicles (12 màn)"
        case .oddOneOut: return "Odd One (12 màn)"
        }
    }
    
    var emoji: String {
        switch self {
        case .shapes: return "🟡"
        case .colors: return "🎨"
        case .sizes: return "📏"
        case .puzzle: return "🧩"
        case .counting: return "🎈"
        case .feedAnimal: return "🍖"
        case .vehicles: return "🚗"
        case .oddOneOut: return "🔍"
        }
    }
    
    var themeColor: Color {
        switch self {
        case .shapes: return Color(red: 0.98, green: 0.44, blue: 0.52)
        case .colors: return Color(red: 0.38, green: 0.76, blue: 0.97)
        case .sizes: return Color(red: 0.52, green: 0.82, blue: 0.48)
        case .puzzle: return Color(red: 0.99, green: 0.72, blue: 0.28)
        case .counting: return Color(red: 0.72, green: 0.54, blue: 0.93)
        case .feedAnimal: return Color(red: 0.99, green: 0.85, blue: 0.35)
        case .vehicles: return Color(red: 0.40, green: 0.85, blue: 0.95)
        case .oddOneOut: return Color(red: 0.95, green: 0.45, blue: 0.72)
        }
    }
    
    var totalLevels: Int { 12 }
}

// MARK: - Shapes
enum ShapeType: String, CaseIterable, Identifiable {
    case circle = "circle"
    case square = "square"
    case triangle = "triangle"
    case star = "star"
    case heart = "heart"
    case diamond = "diamond"
    case moon = "moon"
    case cloud = "cloud"
    
    var id: String { rawValue }
    
    var name: String {
        switch self {
        case .circle: return "Hình tròn"
        case .square: return "Hình vuông"
        case .triangle: return "Hình tam giác"
        case .star: return "Ngôi sao"
        case .heart: return "Trái tim"
        case .diamond: return "Hình thoi"
        case .moon: return "Mặt trăng"
        case .cloud: return "Đám mây"
        }
    }
    
    var color: Color {
        switch self {
        case .circle: return Color(red: 0.95, green: 0.35, blue: 0.35)
        case .square: return Color(red: 0.30, green: 0.65, blue: 0.95)
        case .triangle: return Color(red: 0.40, green: 0.80, blue: 0.45)
        case .star: return Color(red: 0.98, green: 0.78, blue: 0.25)
        case .heart: return Color(red: 0.92, green: 0.42, blue: 0.65)
        case .diamond: return Color(red: 0.65, green: 0.45, blue: 0.95)
        case .moon: return Color(red: 0.98, green: 0.85, blue: 0.30)
        case .cloud: return Color(red: 0.45, green: 0.78, blue: 0.98)
        }
    }
}

struct ShapeItem: Identifiable, Equatable {
    let id: UUID = UUID()
    let type: ShapeType
    var isMatched: Bool = false
    var currentOffset: CGSize = .zero
}

// MARK: - Color Game Item
enum GameColor: String, CaseIterable, Identifiable {
    case red, blue, yellow, green, purple, orange, pink
    var id: String { rawValue }
    
    var name: String {
        switch self {
        case .red: return "Đỏ"
        case .blue: return "Xanh"
        case .yellow: return "Vàng"
        case .green: return "Lục"
        case .purple: return "Tím"
        case .orange: return "Cam"
        case .pink: return "Hồng"
        }
    }
    
    var color: Color {
        switch self {
        case .red: return Color(red: 0.95, green: 0.32, blue: 0.32)
        case .blue: return Color(red: 0.28, green: 0.65, blue: 0.95)
        case .yellow: return Color(red: 0.98, green: 0.82, blue: 0.22)
        case .green: return Color(red: 0.35, green: 0.80, blue: 0.42)
        case .purple: return Color(red: 0.68, green: 0.42, blue: 0.92)
        case .orange: return Color(red: 0.98, green: 0.55, blue: 0.22)
        case .pink: return Color(red: 0.95, green: 0.50, blue: 0.70)
        }
    }
}

struct ColorObjectItem: Identifiable, Equatable {
    let id: UUID = UUID()
    let color: GameColor
    let emoji: String
    var isMatched: Bool = false
    var currentOffset: CGSize = .zero
}

// MARK: - Size Item
struct SizeItem: Identifiable, Equatable {
    let id: UUID = UUID()
    let sizeRank: Int
    let emoji: String
    var isMatched: Bool = false
}

// MARK: - Animal Puzzle
struct PuzzlePiece: Identifiable, Equatable {
    let id: Int
    let name: String
    let icon: String
    var isPlaced: Bool = false
}

// MARK: - Feed Animals
struct AnimalFoodItem: Identifiable, Equatable {
    let id: Int
    let animalName: String
    let animalEmoji: String
    let foodName: String
    let foodEmoji: String
    var isFed: Bool = false
}

// MARK: - Vehicles
enum TransportType: String, CaseIterable, Identifiable {
    case land, air, water
    var id: String { rawValue }
    var name: String {
        switch self {
        case .land: return "Đường bộ"
        case .air: return "Đường bay"
        case .water: return "Đường thủy"
        }
    }
    var emoji: String {
        switch self {
        case .land: return "🚗"
        case .air: return "✈️"
        case .water: return "🚢"
        }
    }
}

struct VehicleModel: Identifiable, Equatable {
    let id: Int
    let name: String
    let emoji: String
    let type: TransportType
    var isMatched: Bool = false
}

// MARK: - Odd One Out
struct OddItem: Identifiable, Equatable {
    let id: Int
    let emoji: String
    let isOdd: Bool
}
