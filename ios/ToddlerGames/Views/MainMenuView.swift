import SwiftUI

struct MainMenuView: View {
    @StateObject private var soundManager = SoundManager.shared
    @State private var selectedGame: GameType? = nil
    @State private var showParentalGate: Bool = false
    @State private var showSettings: Bool = false
    
    // Cột hiển thị: cố định 2 cột đều nhau chuẩn giao diện như Android
    let columns = [
        GridItem(.flexible(), spacing: 14),
        GridItem(.flexible(), spacing: 14)
    ]
    
    var body: some View {
        ZStack {
            LinearGradient(
                colors: [Color(red: 0.98, green: 0.95, blue: 0.90), Color(red: 0.90, green: 0.95, blue: 0.98)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 8) {
                // Top Action Bar
                HStack {
                    // Sound toggle
                    Button(action: {
                        soundManager.isMuted.toggle()
                        soundManager.playPopSound()
                    }) {
                        Image(systemName: soundManager.isMuted ? "speaker.slash.fill" : "speaker.wave.2.fill")
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(.white)
                            .padding(10)
                            .background(Circle().fill(Color.orange))
                            .shadow(color: .orange.opacity(0.3), radius: 3, x: 0, y: 2)
                    }
                    
                    Spacer()
                    
                    // Parental Gate Settings Button
                    Button(action: {
                        soundManager.playPopSound()
                        showParentalGate = true
                    }) {
                        HStack(spacing: 6) {
                            Image(systemName: "lock.fill")
                                .font(.system(size: 13, weight: .bold))
                            Text("Phụ huynh")
                                .font(.system(size: 14, weight: .bold, design: .rounded))
                        }
                        .foregroundColor(.white)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 8)
                        .background(Capsule().fill(Color(red: 0.58, green: 0.35, blue: 0.92)))
                        .shadow(color: Color.purple.opacity(0.3), radius: 3, x: 0, y: 2)
                    }
                }
                .padding(.horizontal, 20)
                .padding(.top, 4)
                
                // Mascot & App Title
                VStack(spacing: 3) {
                    Text("🐻 ✨ 🎈")
                        .font(.system(size: 32))
                    
                    Text("Trò Chơi Cho Bé")
                        .font(.system(size: 26, weight: .heavy, design: .rounded))
                        .foregroundColor(Color(red: 0.22, green: 0.25, blue: 0.42))
                    
                    Text("8 Chủ Đề • Mỗi Chủ Đề 12 Màn Chơi")
                        .font(.system(size: 13, weight: .bold, design: .rounded))
                        .foregroundColor(Color(red: 0.15, green: 0.55, blue: 0.95))
                }
                .padding(.bottom, 2)
                
                // Game Selection Grid (8 categories)
                ScrollView(showsIndicators: false) {
                    LazyVGrid(columns: columns, spacing: 14) {
                        ForEach(GameType.allCases) { game in
                            Button(action: {
                                soundManager.playPopSound()
                                selectedGame = game
                            }) {
                                GameCardView(game: game)
                            }
                            .buttonStyle(BouncyButtonStyle())
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.vertical, 6)
                }
            }
            
            // Parental Gate Modal
            if showParentalGate {
                ParentalGateView(onSuccess: {
                    showParentalGate = false
                    showSettings = true
                })
            }
        }
        .fullScreenCover(item: $selectedGame) { game in
            switch game {
            case .shapes:
                ShapeSortingGameView()
            case .colors:
                ColorMatchingGameView()
            case .sizes:
                SizeOrderingGameView()
            case .puzzle:
                AnimalPuzzleGameView()
            case .counting:
                CountingGameView()
            case .feedAnimal:
                FeedAnimalGameView()
            case .vehicles:
                VehicleSortingGameView()
            case .oddOneOut:
                OddOneOutGameView()
            }
        }
        .sheet(isPresented: $showSettings) {
            ParentSettingsView()
        }
    }
}

// MARK: - Game Card View
struct GameCardView: View {
    let game: GameType
    
    var body: some View {
        VStack(spacing: 8) {
            BoxEmojiView(emoji: game.emoji, color: game.themeColor)
            
            VStack(spacing: 3) {
                Text(game.title)
                    .font(.system(size: 15, weight: .bold, design: .rounded))
                    .foregroundColor(Color(red: 0.2, green: 0.22, blue: 0.32))
                    .lineLimit(1)
                    .minimumScaleFactor(0.8)
                
                Text(game.englishTitle)
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(Color.gray)
                    .lineLimit(1)
                    .minimumScaleFactor(0.8)
            }
        }
        .frame(maxWidth: .infinity)
        .frame(height: 122)
        .background(
            RoundedRectangle(cornerRadius: 22)
                .fill(Color.white)
                .shadow(color: Color.black.opacity(0.06), radius: 6, x: 0, y: 3)
        )
    }
}

struct BoxEmojiView: View {
    let emoji: String
    let color: Color
    
    var body: some View {
        ZStack {
            Circle()
                .fill(color.opacity(0.22))
                .frame(width: 52, height: 52)
            Text(emoji)
                .font(.system(size: 26))
        }
    }
}

// MARK: - Parent Settings Modal
struct ParentSettingsView: View {
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Âm thanh & Hiệu ứng")) {
                    Toggle("Nhạc nền & Hiệu ứng âm thanh", isOn: .constant(true))
                    Toggle("Rung phản hồi (Haptics)", isOn: .constant(true))
                }
                
                Section(header: Text("Thông tin ứng dụng")) {
                    HStack {
                        Text("Phiên bản")
                        Spacer()
                        Text("1.2.0")
                            .foregroundColor(.secondary)
                    }
                    HStack {
                        Text("Tổng số trò chơi")
                        Spacer()
                        Text("8 chủ đề (96 màn chơi)")
                            .foregroundColor(.secondary)
                    }
                    HStack {
                        Text("Độ tuổi phù hợp")
                        Spacer()
                        Text("2 – 5 tuổi")
                            .foregroundColor(.secondary)
                    }
                }
            }
            .navigationTitle("Cài Đặt Phụ Huynh")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Đóng") {
                        dismiss()
                    }
                }
            }
        }
    }
}

struct BouncyButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.92 : 1.0)
            .animation(.spring(response: 0.3, dampingFraction: 0.6), value: configuration.isPressed)
    }
}
