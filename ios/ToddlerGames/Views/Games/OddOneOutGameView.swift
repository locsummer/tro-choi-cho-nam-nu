import SwiftUI

struct OddOneOutGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelQuestions: [[(id: Int, emoji: String, isOdd: Bool)]] = [
        [(1, "🍎", false), (2, "🍎", false), (3, "🍎", false), (4, "🚗", true)],
        [(1, "🐦", false), (2, "🐟", true), (3, "🐦", false), (4, "🐦", false)],
        [(1, "🔴", false), (2, "🔴", false), (3, "🟡", true), (4, "🔴", false)],
        [(1, "🐱", false), (2, "🐱", false), (3, "🍌", true), (4, "🐱", false)],
        [(1, "🟦", false), (2, "⭐", true), (3, "🟦", false), (4, "🟦", false)],
        [(1, "✈️", false), (2, "✈️", false), (3, "✈️", false), (4, "🚢", true)],
        [(1, "🐶", false), (2, "🍦", true), (3, "🐶", false), (4, "🐶", false)],
        [(1, "🌸", false), (2, "🌸", false), (3, "🐸", true), (4, "🌸", false)],
        [(1, "🎈", false), (2, "🎈", false), (3, "🍕", true), (4, "🎈", false)],
        [(1, "🎩", false), (2, "🌲", true), (3, "🎩", false), (4, "🎩", false)],
        [(1, "🍬", false), (2, "🔑", true), (3, "🍬", false), (4, "🍬", false)],
        [(1, "⭐", false), (2, "⭐", false), (3, "⭐", false), (4, "💖", true)]
    ]
    
    @State private var currentLevel: Int = 1
    @State private var choices: [OddItem] = []
    @State private var errorItemId: Int? = nil
    @State private var showCelebration: Bool = false
    
    var body: some View {
        ZStack {
            Color(red: 0.99, green: 0.95, blue: 0.97)
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
                        Text("🔍 Tìm Hình Khác Biệt")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.6, green: 0.1, blue: 0.35))
                        Text("Màn \(currentLevel) / \(totalLevels)")
                            .font(.caption.bold())
                            .foregroundColor(.pink)
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
                
                Text("Hình nào khác với các hình còn lại? 🤔")
                    .font(.system(size: 16, weight: .bold, design: .rounded))
                    .foregroundColor(Color(red: 0.5, green: 0.1, blue: 0.3))
                    .padding(.horizontal, 20)
                    .padding(.vertical, 10)
                    .background(Capsule().fill(Color.white).shadow(radius: 3))
                
                Spacer()
                
                // 2x2 Grid of Choices
                LazyVGrid(columns: [GridItem(.flexible(), spacing: 18), GridItem(.flexible(), spacing: 18)], spacing: 18) {
                    ForEach(choices) { item in
                        let isError = errorItemId == item.id
                        Button(action: { selectItem(item) }) {
                            Text(item.emoji)
                                .font(.system(size: 54))
                                .frame(maxWidth: .infinity, minHeight: 110)
                                .background(
                                    RoundedRectangle(cornerRadius: 24)
                                        .fill(isError ? Color.red.opacity(0.2) : Color.white)
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 24)
                                                .stroke(isError ? Color.red : Color.clear, lineWidth: 3)
                                        )
                                        .shadow(color: isError ? Color.red.opacity(0.4) : Color.pink.opacity(0.15), radius: 8, x: 0, y: 4)
                                )
                        }
                        .buttonStyle(PlainButtonStyle())
                    }
                }
                .padding(.horizontal, 24)
                
                Spacer()
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
    
    private func selectItem(_ item: OddItem) {
        if item.isOdd {
            SoundManager.shared.playSuccessSound()
            withAnimation { showCelebration = true }
        } else {
            SoundManager.shared.playErrorSound()
            withAnimation {
                errorItemId = item.id
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
                withAnimation {
                    if errorItemId == item.id {
                        errorItemId = nil
                    }
                }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        errorItemId = nil
        currentLevel = level
        let list = levelQuestions[(level - 1) % levelQuestions.count]
        choices = list.shuffled().map { OddItem(id: $0.id, emoji: $0.emoji, isOdd: $0.isOdd) }
    }
}
