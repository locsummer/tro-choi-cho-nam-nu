import SwiftUI

struct ColorMatchingGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelConfigs: [([GameColor], [(GameColor, String)])] = [
        ([.red, .blue], [(.red, "🍎"), (.blue, "🫐")]),
        ([.yellow, .green], [(.yellow, "🍌"), (.green, "🥑")]),
        ([.red, .blue, .yellow], [(.red, "🚗"), (.blue, "🚙"), (.yellow, "🚕")]),
        ([.red, .blue, .green], [(.red, "🎈"), (.blue, "🎈"), (.green, "🎈")]),
        ([.red, .blue, .yellow, .green], [(.red, "🍓"), (.blue, "🫐"), (.yellow, "🍋"), (.green, "🍏")]),
        ([.red, .blue, .yellow, .green], [(.red, "🦀"), (.blue, "🐳"), (.yellow, "🐥"), (.green, "🐸")]),
        ([.purple, .orange, .pink], [(.purple, "🍬"), (.orange, "🍊"), (.pink, "🌸")]),
        ([.red, .yellow, .pink, .green], [(.red, "🌹"), (.yellow, "🌻"), (.pink, "🌺"), (.green, "🍀")]),
        ([.red, .blue, .yellow, .purple], [(.red, "💎"), (.blue, "💎"), (.yellow, "⭐"), (.purple, "🔮")]),
        ([.red, .yellow, .green, .pink], [(.red, "🍧"), (.yellow, "🍦"), (.green, "🍨"), (.pink, "🍧")]),
        ([.red, .blue, .orange, .green], [(.red, "🦞"), (.blue, "🐬"), (.orange, "🐠"), (.green, "🐢")]),
        ([.red, .blue, .yellow, .green], [(.red, "❤️"), (.blue, "💙"), (.yellow, "💛"), (.green, "💚")])
    ]
    
    @State private var currentLevel: Int = 1
    @State private var colorBaskets: [GameColor] = []
    @State private var items: [ColorObjectItem] = []
    @State private var activeId: UUID? = nil
    @State private var dragOffsets: [UUID: CGSize] = [:]
    @State private var errorColorId: UUID? = nil
    @State private var showCelebration: Bool = false
    
    var body: some View {
        ZStack {
            Color(red: 0.95, green: 0.97, blue: 1.0)
                .ignoresSafeArea()
            
            VStack(spacing: 16) {
                // Top Nav
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
                        Text("🎨 Phân Loại Màu")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.25, green: 0.35, blue: 0.55))
                        Text("Màn \(currentLevel) / \(totalLevels)")
                            .font(.caption.bold())
                            .foregroundColor(.blue)
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
                
                // Color Baskets
                HStack(spacing: 12) {
                    ForEach(colorBaskets) { basket in
                        VStack(spacing: 6) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 18)
                                    .fill(basket.color.opacity(0.18))
                                    .frame(width: colorBaskets.count > 3 ? 64 : 76, height: colorBaskets.count > 3 ? 78 : 90)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 18)
                                            .stroke(basket.color, lineWidth: 2.5)
                                    )
                                
                                VStack(spacing: 2) {
                                    Text("🧺")
                                        .font(.system(size: colorBaskets.count > 3 ? 24 : 30))
                                    
                                    let matched = items.filter { $0.color == basket && $0.isMatched }
                                    if !matched.isEmpty {
                                        Text(matched.first?.emoji ?? "✓")
                                            .font(.caption)
                                    }
                                }
                            }
                            
                            Text(basket.name)
                                .font(.caption.bold())
                                .foregroundColor(basket.color)
                        }
                    }
                }
                .padding(14)
                .background(
                    RoundedRectangle(cornerRadius: 26)
                        .fill(Color.white)
                        .shadow(color: .black.opacity(0.06), radius: 8, x: 0, y: 4)
                )
                
                Spacer()
                
                // Fruit / Item Row
                HStack(spacing: 16) {
                    ForEach(items) { item in
                        if !item.isMatched {
                            let isError = errorColorId == item.id
                            Text(item.emoji)
                                .font(.system(size: items.count > 3 ? 36 : 46))
                                .padding(10)
                                .background(
                                    Circle()
                                        .fill(isError ? Color.red.opacity(0.2) : Color.white)
                                        .overlay(Circle().stroke(isError ? Color.red : Color.clear, lineWidth: 3))
                                        .shadow(color: (isError ? Color.red : item.color.color).opacity(0.35), radius: 6, x: 0, y: 3)
                                )
                                .offset(dragOffsets[item.id] ?? .zero)
                                .scaleEffect(activeId == item.id ? 1.2 : 1.0)
                                .gesture(
                                    DragGesture()
                                        .onChanged { gesture in
                                            activeId = item.id
                                            dragOffsets[item.id] = gesture.translation
                                        }
                                        .onEnded { gesture in
                                            handleDrop(for: item, translation: gesture.translation)
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
    
    private func handleDrop(for item: ColorObjectItem, translation: CGSize) {
        if translation.height < -100 {
            SoundManager.shared.playSuccessSound()
            withAnimation(.spring(response: 0.35, dampingFraction: 0.65)) {
                if let idx = items.firstIndex(where: { $0.id == item.id }) {
                    items[idx].isMatched = true
                }
                dragOffsets[item.id] = .zero
                activeId = nil
            }
            if items.allSatisfy({ $0.isMatched }) {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                    withAnimation { showCelebration = true }
                }
            }
        } else {
            // SAI: Báo đỏ, âm thanh lỗi, rung và bật về vị trí cũ
            SoundManager.shared.playErrorSound()
            errorColorId = item.id
            withAnimation(.spring(response: 0.4, dampingFraction: 0.6)) {
                dragOffsets[item.id] = .zero
                activeId = nil
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
                if errorColorId == item.id {
                    errorColorId = nil
                }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        errorColorId = nil
        dragOffsets.removeAll()
        activeId = nil
        let config = levelConfigs[(level - 1) % levelConfigs.count]
        colorBaskets = config.0
        items = config.1.shuffled().map { ColorObjectItem(color: $0.0, emoji: $0.1) }
    }
}
