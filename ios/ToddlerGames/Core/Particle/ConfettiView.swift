import SwiftUI

struct ConfettiPiece: Identifiable {
    let id = UUID()
    var x: CGFloat
    var y: CGFloat
    let color: Color
    let size: CGFloat
    let rotation: Double
    let speed: Double
    let shape: String // "star.fill", "circle.fill", "heart.fill"
}

struct ConfettiView: View {
    @State private var pieces: [ConfettiPiece] = []
    @State private var timer: Timer? = nil
    
    let colors: [Color] = [.red, .yellow, .blue, .green, .orange, .pink, .purple]
    let shapes = ["star.fill", "circle.fill", "heart.fill", "sparkles"]
    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                ForEach(pieces) { piece in
                    Image(systemName: piece.shape)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: piece.size, height: piece.size)
                        .foregroundColor(piece.color)
                        .position(x: piece.x, y: piece.y)
                        .rotationEffect(.degrees(piece.rotation))
                        .shadow(color: piece.color.opacity(0.5), radius: 3)
                }
            }
            .onAppear {
                spawnInitialConfetti(in: geometry.size)
                startAnimation(in: geometry.size)
            }
            .onDisappear {
                timer?.invalidate()
            }
        }
        .allowsHitTesting(false)
    }
    
    private func spawnInitialConfetti(in size: CGSize) {
        pieces = (0..<45).map { _ in
            ConfettiPiece(
                x: CGFloat.random(in: 0...size.width),
                y: CGFloat.random(in: -100...0),
                color: colors.randomElement() ?? .yellow,
                size: CGFloat.random(in: 14...28),
                rotation: Double.random(in: 0...360),
                speed: Double.random(in: 3...7),
                shape: shapes.randomElement() ?? "star.fill"
            )
        }
    }
    
    private func startAnimation(in size: CGSize) {
        timer = Timer.scheduledTimer(withTimeInterval: 0.03, repeats: true) { _ in
            for i in pieces.indices {
                pieces[i].y += CGFloat(pieces[i].speed)
                if pieces[i].y > size.height + 50 {
                    pieces[i].y = CGFloat.random(in: -80 ... -10)
                    pieces[i].x = CGFloat.random(in: 0...size.width)
                }
            }
        }
    }
}
