import SwiftUI

struct ShapeSortingGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelShapeSets: [[ShapeType]] = [
        [.circle, .square],
        [.triangle, .star],
        [.circle, .triangle, .square],
        [.heart, .star, .circle],
        [.circle, .square, .triangle, .star],
        [.heart, .diamond, .star, .moon],
        [.circle, .cloud, .heart, .star],
        [.square, .diamond, .triangle, .moon],
        [.cloud, .star, .heart, .circle],
        [.diamond, .square, .cloud, .triangle],
        [.moon, .star, .cloud, .heart],
        [.circle, .square, .triangle, .star]
    ]
    
    @State private var currentLevel: Int = 1
    @State private var availableShapes: [ShapeItem] = []
    @State private var targetSlots: [ShapeType] = []
    @State private var activeDraggingId: UUID? = nil
    @State private var dragOffsets: [UUID: CGSize] = [:]
    @State private var errorShapeId: UUID? = nil
    @State private var showCelebration: Bool = false
    
    var body: some View {
        ZStack {
            LinearGradient(
                colors: [Color(red: 0.98, green: 0.94, blue: 0.96), Color(red: 0.94, green: 0.96, blue: 1.0)],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 16) {
                // Top Navigation Bar
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
                        Text("🟡 Hình Khối")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.3, green: 0.3, blue: 0.5))
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
                
                // Target Silhouettes
                HStack(spacing: 16) {
                    ForEach(targetSlots) { shape in
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .fill(Color.gray.opacity(0.15))
                                .frame(width: targetSlots.count > 3 ? 68 : 82, height: targetSlots.count > 3 ? 68 : 82)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 20)
                                        .stroke(Color.gray.opacity(0.3), style: StrokeStyle(lineWidth: 2.5, dash: [5]))
                                )
                            
                            ShapeIcon(type: shape)
                                .foregroundColor(Color.gray.opacity(0.35))
                                .frame(width: 44, height: 44)
                            
                            if isTargetFilled(shape) {
                                ShapeIcon(type: shape)
                                    .foregroundColor(shape.color)
                                    .frame(width: 46, height: 46)
                                    .transition(.scale.combined(with: .opacity))
                            }
                        }
                    }
                }
                .padding(16)
                .background(
                    RoundedRectangle(cornerRadius: 28)
                        .fill(Color.white)
                        .shadow(color: .black.opacity(0.06), radius: 8, x: 0, y: 4)
                )
                
                Spacer()
                
                // Source Pieces
                HStack(spacing: 18) {
                    ForEach(availableShapes) { item in
                        if !item.isMatched {
                            let isError = errorShapeId == item.id
                            ShapeIcon(type: item.type)
                                .foregroundColor(isError ? .red : item.type.color)
                                .frame(width: 60, height: 60)
                                .background(
                                    Circle()
                                        .fill(isError ? Color.red.opacity(0.2) : Color.white)
                                        .overlay(Circle().stroke(isError ? Color.red : Color.clear, lineWidth: 3))
                                        .shadow(color: (isError ? Color.red : item.type.color).opacity(0.35), radius: 6, x: 0, y: 3)
                                )
                                .offset(dragOffsets[item.id] ?? .zero)
                                .scaleEffect(activeDraggingId == item.id ? 1.15 : 1.0)
                                .gesture(
                                    DragGesture()
                                        .onChanged { gesture in
                                            activeDraggingId = item.id
                                            dragOffsets[item.id] = gesture.translation
                                        }
                                        .onEnded { gesture in
                                            checkDrop(for: item, translation: gesture.translation)
                                        }
                                )
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
    
    private func isTargetFilled(_ shape: ShapeType) -> Bool {
        guard let item = availableShapes.first(where: { $0.type == shape }) else { return false }
        return item.isMatched
    }
    
    private func checkDrop(for item: ShapeItem, translation: CGSize) {
        if translation.height < -90 {
            SoundManager.shared.playSuccessSound()
            withAnimation(.spring(response: 0.35, dampingFraction: 0.65)) {
                if let idx = availableShapes.firstIndex(where: { $0.id == item.id }) {
                    availableShapes[idx].isMatched = true
                }
                dragOffsets[item.id] = .zero
                activeDraggingId = nil
            }
            if availableShapes.allSatisfy({ $0.isMatched }) {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                    withAnimation { showCelebration = true }
                }
            }
        } else {
            // SAI: Báo đỏ, âm thanh lỗi, rung và bật về vị trí cũ để làm lại
            SoundManager.shared.playErrorSound()
            errorShapeId = item.id
            withAnimation(.spring(response: 0.4, dampingFraction: 0.6)) {
                dragOffsets[item.id] = .zero
                activeDraggingId = nil
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
                if errorShapeId == item.id {
                    errorShapeId = nil
                }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        errorShapeId = nil
        dragOffsets.removeAll()
        activeDraggingId = nil
        let set = levelShapeSets[(level - 1) % levelShapeSets.count]
        targetSlots = set
        availableShapes = set.shuffled().map { ShapeItem(type: $0) }
    }
}
