import SwiftUI

struct CountingItemModel: Identifiable {
    let id: Int
    let emoji: String
    var isCounted: Bool = false
}

struct CountingGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelConfigs: [(Int, String)] = [
        (3, "🎈"), (3, "🍎"), (4, "⭐"), (4, "🐥"),
        (5, "🍓"), (5, "🚗"), (6, "🍬"), (6, "🐟"),
        (7, "🌸"), (8, "🍪"), (9, "💎"), (10, "🦋")
    ]
    
    @State private var currentLevel: Int = 1
    @State private var targetCount: Int = 3
    @State private var countedCount: Int = 0
    @State private var items: [CountingItemModel] = []
    @State private var showCelebration: Bool = false
    
    var body: some View {
        ZStack {
            LinearGradient(
                colors: [Color(red: 0.85, green: 0.94, blue: 1.0), Color(red: 0.95, green: 0.98, blue: 1.0)],
                startPoint: .top,
                endPoint: .bottom
            )
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
                        Text("🎈 Bé Học Đếm")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.35, green: 0.25, blue: 0.55))
                        Text("Màn \(currentLevel) / \(totalLevels)")
                            .font(.caption.bold())
                            .foregroundColor(.purple)
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
                
                // Big Counter Display
                Text("Đã đếm: \(countedCount) / \(targetCount)")
                    .font(.system(size: 24, weight: .heavy, design: .rounded))
                    .foregroundColor(.purple)
                    .padding(.horizontal, 24)
                    .padding(.vertical, 8)
                    .background(Capsule().fill(Color.white).shadow(radius: 3))
                
                Spacer()
                
                // Grid of items to tap
                LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 14), count: targetCount <= 4 ? 2 : (targetCount <= 6 ? 3 : 4)), spacing: 14) {
                    ForEach(items) { item in
                        if !item.isCounted {
                            Button(action: { countItem(item) }) {
                                Text(item.emoji)
                                    .font(.system(size: 38))
                                    .frame(width: 65, height: 65)
                                    .background(
                                        Circle()
                                            .fill(Color.white)
                                            .shadow(color: .purple.opacity(0.3), radius: 6, x: 0, y: 3)
                                    )
                            }
                            .buttonStyle(PlainButtonStyle())
                        } else {
                            Text("✨")
                                .font(.title)
                                .frame(width: 65, height: 65)
                                .background(Circle().fill(Color.purple.opacity(0.12)))
                        }
                    }
                }
                .padding(.horizontal, 20)
                
                Spacer()
                
                Text("Chạm vào từng hình để đếm nhé! 👇")
                    .font(.caption.bold())
                    .foregroundColor(Color.gray.opacity(0.8))
                    .padding(.bottom, 24)
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
    
    private func countItem(_ item: CountingItemModel) {
        SoundManager.shared.playPopSound()
        withAnimation(.spring(response: 0.3, dampingFraction: 0.6)) {
            if let idx = items.firstIndex(where: { $0.id == item.id }) {
                items[idx].isCounted = true
                countedCount += 1
            }
        }
        if countedCount >= targetCount {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                withAnimation { showCelebration = true }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        countedCount = 0
        let config = levelConfigs[(level - 1) % levelConfigs.count]
        targetCount = config.0
        items = (1...config.0).map { CountingItemModel(id: $0, emoji: config.1) }
    }
}
