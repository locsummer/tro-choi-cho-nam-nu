import SwiftUI

struct FeedAnimalGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelPairs: [[(id: Int, animalName: String, animalEmoji: String, foodName: String, foodEmoji: String)]] = [
        [(1, "Khỉ", "🐒", "Chuối", "🍌"), (2, "Thỏ", "🐇", "Cà rốt", "🥕")],
        [(1, "Mèo", "🐱", "Cá", "🐟"), (2, "Cún", "🐶", "Xương", "🍖")],
        [(1, "Gấu trúc", "🐼", "Tre trúc", "🎋"), (2, "Chuột", "🐭", "Phô mai", "🧀")],
        [(1, "Bò sữa", "🐄", "Bó cỏ", "🌾"), (2, "Gấu nâu", "🐻", "Mật ong", "🍯")],
        [(1, "Chim non", "🐦", "Sâu bọ", "🐛"), (2, "Voi con", "🐘", "Quả táo", "🍎")],
        [(1, "Khỉ", "🐒", "Chuối", "🍌"), (2, "Thỏ", "🐇", "Cà rốt", "🥕"), (3, "Mèo", "🐱", "Cá", "🐟")],
        [(1, "Cún", "🐶", "Xương", "🍖"), (2, "Gấu trúc", "🐼", "Tre", "🎋"), (3, "Chuột", "🐭", "Phô mai", "🧀")],
        [(1, "Chú ngựa", "🐎", "Cà rốt", "🥕"), (2, "Sóc con", "🐿️", "Hạt dẻ", "🌰"), (3, "Bướm", "🦋", "Mật hoa", "🌸")],
        [(1, "Ếch cốm", "🐸", "Ruồi", "🪰"), (2, "Vịt con", "🦆", "Bánh mì", "🍞"), (3, "Hải cẩu", "🦭", "Cá", "🐟")],
        [(1, "Khỉ", "🐒", "Chuối", "🍌"), (2, "Mèo", "🐱", "Cá", "🐟"), (3, "Cún", "🐶", "Xương", "🍖"), (4, "Thỏ", "🐇", "Cà rốt", "🥕")],
        [(1, "Gấu", "🐻", "Mật ong", "🍯"), (2, "Chuột", "🐭", "Phô mai", "🧀"), (3, "Bò", "🐄", "Cỏ", "🌾"), (4, "Chim", "🐦", "Sâu", "🐛")],
        [(1, "Sư tử", "🦁", "Thịt đùi", "🥩"), (2, "Voi", "🐘", "Dưa hấu", "🍉"), (3, "Gấu trúc", "🐼", "Trúc", "🎋"), (4, "Khỉ", "🐒", "Chuối", "🍌")]
    ]
    
    @State private var currentLevel: Int = 1
    @State private var animals: [AnimalFoodItem] = []
    @State private var foods: [AnimalFoodItem] = []
    @State private var activeFoodId: Int? = nil
    @State private var errorFoodId: Int? = nil
    @State private var dragOffsets: [Int: CGSize] = [:]
    @State private var showCelebration: Bool = false
    
    var body: some View {
        ZStack {
            Color(red: 0.99, green: 0.98, blue: 0.90)
                .ignoresSafeArea()
            
            VStack(spacing: 16) {
                // Header
                HStack {
                    Button(action: {
                        SoundManager.shared.playPopSound()
                        dismiss()
                    }) {
                        Image(systemName: "house.fill")
                            .font(.title3)
                            .foregroundColor(.white)
                            .padding(12)
                            .background(Circle().fill(Color.orange))
                            .shadow(radius: 3)
                    }
                    
                    Spacer()
                    
                    VStack(spacing: 2) {
                        Text("🍖 Bé Cho Thú Ăn")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.45, green: 0.35, blue: 0.1))
                        Text("Màn \(currentLevel) / \(totalLevels)")
                            .font(.caption.bold())
                            .foregroundColor(.orange)
                    }
                    
                    Spacer()
                    
                    Button(action: { loadLevel(currentLevel) }) {
                        Image(systemName: "arrow.clockwise")
                            .font(.title3)
                            .foregroundColor(.white)
                            .padding(12)
                            .background(Circle().fill(Color.blue))
                            .shadow(radius: 3)
                    }
                }
                .padding(.horizontal, 20)
                .padding(.top, 10)
                
                Spacer()
                
                // Animals Targets
                HStack(spacing: 14) {
                    ForEach(animals) { animal in
                        VStack(spacing: 4) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 18)
                                    .fill(animal.isFed ? Color.green.opacity(0.18) : Color.yellow.opacity(0.18))
                                    .frame(width: animals.count > 3 ? 64 : 76, height: animals.count > 3 ? 74 : 86)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 18)
                                            .stroke(animal.isFed ? Color.green : Color.orange, lineWidth: 2)
                                    )
                                
                                VStack {
                                    Text(animal.isFed ? "😋" : animal.animalEmoji)
                                        .font(.system(size: animals.count > 3 ? 28 : 36))
                                    if animal.isFed {
                                        Text(animal.foodEmoji)
                                            .font(.caption)
                                    }
                                }
                            }
                            
                            Text(animal.animalName)
                                .font(.caption.bold())
                                .foregroundColor(Color(red: 0.4, green: 0.3, blue: 0.1))
                        }
                    }
                }
                .padding(16)
                .background(
                    RoundedRectangle(cornerRadius: 26)
                        .fill(Color.white)
                        .shadow(color: .black.opacity(0.06), radius: 8, x: 0, y: 4)
                )
                
                Spacer()
                
                // Foods to drag
                HStack(spacing: 16) {
                    ForEach(foods) { food in
                        let isError = errorFoodId == food.id
                        if !food.isFed {
                            Text(food.foodEmoji)
                                .font(.system(size: foods.count > 3 ? 34 : 44))
                                .padding(10)
                                .background(
                                    Circle()
                                        .fill(isError ? Color.red.opacity(0.2) : Color.white)
                                        .overlay(
                                            Circle()
                                                .stroke(isError ? Color.red : Color.clear, lineWidth: 3)
                                        )
                                        .shadow(color: isError ? Color.red.opacity(0.4) : Color.orange.opacity(0.35), radius: 6, x: 0, y: 3)
                                )
                                .offset(dragOffsets[food.id] ?? .zero)
                                .scaleEffect(activeFoodId == food.id ? 1.2 : 1.0)
                                .gesture(
                                    DragGesture()
                                        .onChanged { gesture in
                                            activeFoodId = food.id
                                            dragOffsets[food.id] = gesture.translation
                                        }
                                        .onEnded { gesture in
                                            handleDrop(for: food, translation: gesture.translation)
                                        }
                                )
                        } else {
                            Color.clear.frame(width: 58, height: 58)
                        }
                    }
                }
                .padding(.bottom, 36)
            }
            
            if showCelebration {
                GameCelebrationOverlay(
                    currentLevel: currentLevel,
                    totalLevels: totalLevels,
                    onReplay: { loadLevel(currentLevel) },
                    onNextLevel: {
                        if currentLevel < totalLevels {
                            loadLevel(currentLevel + 1)
                        } else {
                            loadLevel(1)
                        }
                    },
                    onMenu: { dismiss() }
                )
            }
        }
        .onAppear {
            loadLevel(1)
        }
    }
    
    private func handleDrop(for food: AnimalFoodItem, translation: CGSize) {
        if translation.height < -100 {
            SoundManager.shared.playSuccessSound()
            withAnimation(.spring(response: 0.35, dampingFraction: 0.65)) {
                if let idx = animals.firstIndex(where: { $0.id == food.id }) {
                    animals[idx].isFed = true
                }
                if let fIdx = foods.firstIndex(where: { $0.id == food.id }) {
                    foods[fIdx].isFed = true
                }
                dragOffsets[food.id] = .zero
                activeFoodId = nil
            }
            if animals.allSatisfy({ $0.isFed }) {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                    withAnimation { showCelebration = true }
                }
            }
        } else {
            SoundManager.shared.playErrorSound()
            withAnimation {
                errorFoodId = food.id
            }
            withAnimation(.spring(response: 0.4, dampingFraction: 0.6)) {
                dragOffsets[food.id] = .zero
                activeFoodId = nil
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
                withAnimation {
                    if errorFoodId == food.id {
                        errorFoodId = nil
                    }
                }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        dragOffsets.removeAll()
        activeFoodId = nil
        errorFoodId = nil
        let pairs = levelPairs[(level - 1) % levelPairs.count]
        animals = pairs.map { AnimalFoodItem(id: $0.id, animalName: $0.animalName, animalEmoji: $0.animalEmoji, foodName: $0.foodName, foodEmoji: $0.foodEmoji, isFed: false) }
        foods = pairs.shuffled().map { AnimalFoodItem(id: $0.id, animalName: $0.animalName, animalEmoji: $0.animalEmoji, foodName: $0.foodName, foodEmoji: $0.foodEmoji, isFed: false) }
    }
}
