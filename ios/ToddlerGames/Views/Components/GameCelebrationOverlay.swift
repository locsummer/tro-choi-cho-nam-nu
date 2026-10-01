import SwiftUI

struct GameCelebrationOverlay: View {
    let currentLevel: Int
    let totalLevels: Int
    let onReplay: () -> Void
    let onNextLevel: () -> Void
    let onMenu: () -> Void
    
    @State private var scale: CGFloat = 0.4
    @State private var opacity: Double = 0.0
    
    var isLastLevel: Bool { currentLevel >= totalLevels }
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.4)
                .ignoresSafeArea()
            
            ConfettiView()
            
            VStack(spacing: 16) {
                // Star badges
                HStack(spacing: 12) {
                    Image(systemName: "star.fill")
                        .foregroundColor(.yellow)
                        .font(.system(size: 34))
                        .rotationEffect(.degrees(-15))
                    Image(systemName: "star.fill")
                        .foregroundColor(.yellow)
                        .font(.system(size: 48))
                        .offset(y: -8)
                    Image(systemName: "star.fill")
                        .foregroundColor(.yellow)
                        .font(.system(size: 34))
                        .rotationEffect(.degrees(15))
                }
                .shadow(color: .orange.opacity(0.6), radius: 8)
                
                Text(isLastLevel ? "HOÀN THÀNH CHỦ ĐỀ! 🏆" : "BÉ GIỎI QUÁ! 🎉")
                    .font(.system(size: 24, weight: .heavy, design: .rounded))
                    .foregroundColor(Color(red: 0.2, green: 0.2, blue: 0.4))
                
                // Level badge
                Text("Màn \(currentLevel) / \(totalLevels)")
                    .font(.subheadline.bold())
                    .foregroundColor(.orange)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 6)
                    .background(Capsule().fill(Color.orange.opacity(0.15)))
                
                Text(isLastLevel ? "Bé đã xuất sắc vượt qua toàn bộ màn chơi!" : "Sẵn sàng cho thử thách tiếp theo chưa?")
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                
                VStack(spacing: 10) {
                    // Next Level Button
                    Button(action: {
                        SoundManager.shared.playPopSound()
                        onNextLevel()
                    }) {
                        HStack {
                            Text(isLastLevel ? "Chơi lại từ đầu" : "Sang Màn Tiếp Theo")
                                .font(.headline.bold())
                            Image(systemName: "arrow.right.circle.fill")
                                .font(.title3)
                        }
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity, minHeight: 46)
                        .background(Color.green)
                        .cornerRadius(16)
                    }
                    
                    HStack(spacing: 12) {
                        // Replay current
                        Button(action: {
                            SoundManager.shared.playPopSound()
                            onReplay()
                        }) {
                            HStack {
                                Image(systemName: "arrow.counterclockwise")
                                Text("Chơi lại")
                            }
                            .font(.subheadline.bold())
                            .foregroundColor(.blue)
                            .frame(maxWidth: .infinity, minHeight: 42)
                            .background(Color.blue.opacity(0.12))
                            .cornerRadius(14)
                        }
                        
                        // Menu out
                        Button(action: {
                            SoundManager.shared.playPopSound()
                            onMenu()
                        }) {
                            HStack {
                                Image(systemName: "house.fill")
                                Text("Menu")
                            }
                            .font(.subheadline.bold())
                            .foregroundColor(.orange)
                            .frame(maxWidth: .infinity, minHeight: 42)
                            .background(Color.orange.opacity(0.12))
                            .cornerRadius(14)
                        }
                    }
                }
                .padding(.top, 6)
            }
            .padding(26)
            .background(
                RoundedRectangle(cornerRadius: 28)
                    .fill(Color.white)
                    .shadow(color: .black.opacity(0.2), radius: 25, x: 0, y: 10)
            )
            .frame(maxWidth: 320)
            .scaleEffect(scale)
            .opacity(opacity)
            .onAppear {
                SoundManager.shared.playCelebrationFanfare()
                withAnimation(.spring(response: 0.45, dampingFraction: 0.65)) {
                    scale = 1.0
                    opacity = 1.0
                }
            }
        }
    }
}
