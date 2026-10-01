import SwiftUI

struct AnimalPuzzleGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelPuzzles: [[(id: Int, name: String, icon: String)]] = [
        [(1, "Sư tử", "🦁"), (2, "Voi con", "🐘"), (3, "Hươu cao cổ", "🦒"), (4, "Khỉ con", "🐒")],
        [(1, "Bò sữa", "🐄"), (2, "Chú heo", "🐖"), (3, "Cừu con", "🐑"), (4, "Gà trống", "🐓")],
        [(1, "Cá voi", "🐳"), (2, "Cá heo", "🐬"), (3, "Bạch tuộc", "🐙"), (4, "Cua biển", "🦀")],
        [(1, "Gấu nâu", "🐻"), (2, "Cáo đỏ", "🦊"), (3, "Thỏ trắng", "🐇"), (4, "Sóc con", "🐿️")],
        [(1, "Cún con", "🐶"), (2, "Mèo con", "🐱"), (3, "Chuột hamster", "🐹"), (4, "Vẹt xanh", "🦜")],
        [(1, "Cú mèo", "🦉"), (2, "Hồng hạc", "🦩"), (3, "Đại bàng", "🦅"), (4, "Vịt con", "🦆")],
        [(1, "Bướm hoa", "🦋"), (2, "Ong mật", "🐝"), (3, "Bọ rùa", "🐞"), (4, "Ốc sên", "🐌")],
        [(1, "Chim cánh cụt", "🐧"), (2, "Gấu bắc cực", "🐻‍❄️"), (3, "Hải cẩu", "🦭"), (4, "Tuần lộc", "🦌")],
        [(1, "Ngựa vằn", "🦓"), (2, "Hà mã", "🦛"), (3, "Tê giác", "🦏"), (4, "Báo gấm", "🐆")],
        [(1, "Khủng long T-Rex", "🦖"), (2, "Cổ dài", "🦕"), (3, "Rồng lửa", "🐉"), (4, "Trứng", "🥚")],
        [(1, "Ếch xanh", "🐸"), (2, "Rùa biển", "🐢"), (3, "Tắc kè", "🦎"), (4, "Cá sấu", "🐊")],
        [(1, "Kỳ lân", "🦄"), (2, "Gấu trúc", "🐼"), (3, "Koala", "🐨"), (4, "Hổ con", "🐯")]
    ]
    
    @State private var currentLevel: Int = 1
    @State private var pieces: [PuzzlePiece] = []
    @State private var showCelebration: Bool = false
    
    var currentAnimals: [(id: Int, name: String, icon: String)] {
        levelPuzzles[(currentLevel - 1) % levelPuzzles.count]
    }
    
    var body: some View {
        ZStack {
            Color(red: 0.99, green: 0.96, blue: 0.90)
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
                        Text("🧩 Ghép Đôi Động Vật")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.5, green: 0.35, blue: 0.1))
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
                
                // 2x2 Animal Silhouette Board
                LazyVGrid(columns: [GridItem(.flexible(), spacing: 14), GridItem(.flexible(), spacing: 14)], spacing: 14) {
                    ForEach(currentAnimals, id: \.id) { animal in
                        let isSolved = pieces.first(where: { $0.id == animal.id })?.isPlaced ?? false
                        
                        ZStack {
                            RoundedRectangle(cornerRadius: 22)
                                .fill(isSolved ? Color.yellow.opacity(0.18) : Color.gray.opacity(0.12))
                                .frame(height: 100)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 22)
                                        .stroke(isSolved ? Color.orange : Color.gray.opacity(0.3), lineWidth: 2.5)
                                )
                            
                            VStack(spacing: 4) {
                                if isSolved {
                                    Text(animal.icon)
                                        .font(.system(size: 42))
                                        .transition(.scale.combined(with: .opacity))
                                } else {
                                    Text("❓")
                                        .font(.system(size: 30))
                                        .opacity(0.35)
                                }
                                
                                Text(animal.name)
                                    .font(.caption.bold())
                                    .foregroundColor(isSolved ? .orange : .gray)
                            }
                        }
                    }
                }
                .padding(20)
                .background(
                    RoundedRectangle(cornerRadius: 28)
                        .fill(Color.white)
                        .shadow(color: .black.opacity(0.06), radius: 8, x: 0, y: 4)
                )
                .padding(.horizontal, 20)
                
                Spacer()
                
                // Available Pieces to Place
                HStack(spacing: 16) {
                    ForEach(pieces) { piece in
                        if !piece.isPlaced {
                            Button(action: { placePiece(piece) }) {
                                Text(piece.icon)
                                    .font(.system(size: 40))
                                    .padding(10)
                                    .background(
                                        Circle()
                                            .fill(Color.white)
                                            .shadow(color: .orange.opacity(0.35), radius: 6, x: 0, y: 3)
                                    )
                            }
                            .buttonStyle(PlainButtonStyle())
                        } else {
                            Color.clear.frame(width: 55, height: 55)
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
    
    private func placePiece(_ piece: PuzzlePiece) {
        SoundManager.shared.playSuccessSound()
        withAnimation(.spring(response: 0.35, dampingFraction: 0.65)) {
            if let idx = pieces.firstIndex(where: { $0.id == piece.id }) {
                pieces[idx].isPlaced = true
            }
        }
        
        if pieces.allSatisfy({ $0.isPlaced }) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                withAnimation { showCelebration = true }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        let animals = levelPuzzles[(level - 1) % levelPuzzles.count]
        pieces = animals.map { PuzzlePiece(id: $0.id, name: $0.name, icon: $0.icon, isPlaced: false) }.shuffled()
    }
}
