import SwiftUI

struct SizeOrderingGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelEmojis: [String] = ["🐻", "🐟", "🎂", "🌲", "🚗", "🏠", "⚽", "⛄", "🍎", "⭐", "🚀", "🐧"]
    
    @State private var currentLevel: Int = 1
    @State private var items: [SizeItem] = []
    @State private var placedSlots: [Int?] = [nil, nil, nil]
    @State private var errorSizeId: UUID? = nil
    @State private var showCelebration: Bool = false
    
    var currentEmoji: String { levelEmojis[(currentLevel - 1) % levelEmojis.count] }
    
    var body: some View {
        ZStack {
            Color(red: 0.94, green: 0.98, blue: 0.94)
                .ignoresSafeArea()
            
            VStack(spacing: 16) {
                // Top Bar
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
                        Text("📏 Xếp Kích Thước")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.2, green: 0.45, blue: 0.25))
                        Text("Màn \(currentLevel) / \(totalLevels)")
                            .font(.caption.bold())
                            .foregroundColor(.green)
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
                
                // Drop Stands
                HStack(alignment: .bottom, spacing: 16) {
                    ForEach(0..<3, id: \.self) { index in
                        let rank = index + 1
                        VStack(spacing: 8) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 18)
                                    .stroke(Color.green.opacity(0.4), style: StrokeStyle(lineWidth: 2.5, dash: [5]))
                                    .background(Color.white.opacity(0.85))
                                    .frame(width: CGFloat(68 + index * 18), height: CGFloat(78 + index * 20))
                                
                                if placedSlots[index] != nil {
                                    Text(currentEmoji)
                                        .font(.system(size: CGFloat(28 + index * 14)))
                                        .transition(.scale.combined(with: .opacity))
                                } else {
                                    Text("\(rank)")
                                        .font(.title3.bold())
                                        .foregroundColor(Color.gray.opacity(0.35))
                                }
                            }
                            
                            Text(rankTitle(rank))
                                .font(.caption.bold())
                                .foregroundColor(.secondary)
                        }
                    }
                }
                .padding(18)
                .background(
                    RoundedRectangle(cornerRadius: 28)
                        .fill(Color.white)
                        .shadow(color: .black.opacity(0.05), radius: 8, x: 0, y: 4)
                )
                
                Spacer()
                
                // Available items to place
                HStack(spacing: 24) {
                    let expectedRank = (placedSlots.firstIndex(where: { $0 == nil }) ?? 0) + 1
                    
                    ForEach(items) { item in
                        if !item.isMatched {
                            let isError = errorSizeId == item.id
                            Button(action: { placeItem(item, expectedRank: expectedRank) }) {
                                Text(item.emoji)
                                    .font(.system(size: CGFloat(28 + (item.sizeRank - 1) * 14)))
                                    .padding(12)
                                    .background(
                                        Circle()
                                            .fill(isError ? Color.red.opacity(0.2) : Color.white)
                                            .overlay(Circle().stroke(isError ? Color.red : Color.clear, lineWidth: 3))
                                            .shadow(color: (isError ? Color.red : Color.green).opacity(0.3), radius: 6, x: 0, y: 3)
                                    )
                            }
                            .buttonStyle(PlainButtonStyle())
                        } else {
                            Color.clear.frame(width: 60, height: 60)
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
    
    private func rankTitle(_ rank: Int) -> String {
        switch rank {
        case 1: return "Nhỏ"
        case 2: return "Vừa"
        default: return "Lớn"
        }
    }
    
    private func placeItem(_ item: SizeItem, expectedRank: Int) {
        if item.sizeRank == expectedRank {
            let targetIndex = item.sizeRank - 1
            SoundManager.shared.playSuccessSound()
            
            withAnimation(.spring(response: 0.35, dampingFraction: 0.65)) {
                placedSlots[targetIndex] = item.sizeRank
                if let idx = items.firstIndex(where: { $0.id == item.id }) {
                    items[idx].isMatched = true
                }
            }
            
            if placedSlots.allSatisfy({ $0 != nil }) {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                    withAnimation { showCelebration = true }
                }
            }
        } else {
            // SAI: Chọn sai thứ tự kích thước -> Báo đỏ, âm thanh lỗi, rung và làm lại
            SoundManager.shared.playErrorSound()
            errorSizeId = item.id
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
                if errorSizeId == item.id {
                    errorSizeId = nil
                }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        errorSizeId = nil
        placedSlots = [nil, nil, nil]
        let emoji = levelEmojis[(level - 1) % levelEmojis.count]
        items = [
            SizeItem(sizeRank: 1, emoji: emoji),
            SizeItem(sizeRank: 2, emoji: emoji),
            SizeItem(sizeRank: 3, emoji: emoji)
        ].shuffled()
    }
}
