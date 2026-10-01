import SwiftUI

struct VehicleSortingGameView: View {
    @Environment(\.dismiss) private var dismiss
    
    let totalLevels = 12
    let levelVehicles: [[(id: Int, name: String, emoji: String, type: TransportType)]] = [
        [(1, "Ô tô", "🚗", .land), (2, "Máy bay", "✈️", .air)],
        [(1, "Tàu thủy", "🚢", .water), (2, "Tàu hỏa", "🚂", .land)],
        [(1, "Trực thăng", "🚁", .air), (2, "Thuyền", "⛵", .water)],
        [(1, "Xe buýt", "🚌", .land), (2, "Tên lửa", "🚀", .air), (3, "Tàu ngầm", "🤿", .water)],
        [(1, "Xe đạp", "🚲", .land), (2, "Khinh khí cầu", "🎈", .air), (3, "Cano", "🚤", .water)],
        [(1, "Xe cứu hỏa", "🚒", .land), (2, "Đĩa bay", "🛸", .air), (3, "Du thuyền", "🛥️", .water)],
        [(1, "Xe cảnh sát", "🚓", .land), (2, "Phản lực", "🛩️", .air), (3, "Phà", "⛴️", .water)],
        [(1, "Máy kéo", "🚜", .land), (2, "Dù lượn", "🪂", .air), (3, "Kayak", "🛶", .water)],
        [(1, "Xe cứu thương", "🚑", .land), (2, "Drone", "🚁", .air), (3, "Mô tô nước", "🚤", .water)],
        [(1, "Taxi", "🚕", .land), (2, "Xe máy", "🛵", .land), (3, "Xe tải", "🚚", .land)],
        [(1, "Máy bay", "✈️", .air), (2, "Trực thăng", "🚁", .air), (3, "Tên lửa", "🚀", .air)],
        [(1, "Xe đua", "🏎️", .land), (2, "Máy bay", "✈️", .air), (3, "Tàu chiến", "🛳️", .water), (4, "Khinh khí cầu", "🎈", .air)]
    ]
    
    @State private var currentLevel: Int = 1
    @State private var activeTypes: [TransportType] = []
    @State private var vehicles: [VehicleModel] = []
    @State private var activeId: Int? = nil
    @State private var errorVehicleId: Int? = nil
    @State private var dragOffsets: [Int: CGSize] = [:]
    @State private var showCelebration: Bool = false
    
    var body: some View {
        ZStack {
            Color(red: 0.90, green: 0.96, blue: 1.0)
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
                        Text("🚗 Phương Tiện Giao Thông")
                            .font(.system(size: 20, weight: .heavy, design: .rounded))
                            .foregroundColor(Color(red: 0.1, green: 0.4, blue: 0.6))
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
                
                // Transport Zones
                HStack(spacing: 14) {
                    ForEach(activeTypes) { type in
                        let matched = vehicles.filter { $0.type == type && $0.isMatched }
                        VStack(spacing: 4) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 20)
                                    .fill(Color.white)
                                    .frame(width: activeTypes.count > 2 ? 80 : 96, height: activeTypes.count > 2 ? 85 : 100)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 20)
                                            .stroke(Color.blue.opacity(0.5), lineWidth: 2)
                                    )
                                
                                VStack {
                                    Text(type.emoji)
                                        .font(.system(size: activeTypes.count > 2 ? 32 : 40))
                                    if !matched.isEmpty {
                                        Text("✓ x\(matched.count)")
                                            .font(.caption.bold())
                                            .foregroundColor(.blue)
                                    }
                                }
                            }
                            
                            Text(type.name)
                                .font(.caption.bold())
                                .foregroundColor(Color(red: 0.1, green: 0.35, blue: 0.6))
                        }
                    }
                }
                .padding(16)
                .background(
                    RoundedRectangle(cornerRadius: 26)
                        .fill(Color.white.opacity(0.8))
                        .shadow(color: .black.opacity(0.06), radius: 8, x: 0, y: 4)
                )
                
                Spacer()
                
                // Draggable Vehicles
                HStack(spacing: 16) {
                    ForEach(vehicles) { v in
                        let isError = errorVehicleId == v.id
                        if !v.isMatched {
                            Text(v.emoji)
                                .font(.system(size: vehicles.count > 3 ? 34 : 44))
                                .padding(10)
                                .background(
                                    Circle()
                                        .fill(isError ? Color.red.opacity(0.2) : Color.white)
                                        .overlay(
                                            Circle()
                                                .stroke(isError ? Color.red : Color.clear, lineWidth: 3)
                                        )
                                        .shadow(color: isError ? Color.red.opacity(0.4) : Color.blue.opacity(0.3), radius: 6, x: 0, y: 3)
                                )
                                .offset(dragOffsets[v.id] ?? .zero)
                                .scaleEffect(activeId == v.id ? 1.2 : 1.0)
                                .gesture(
                                    DragGesture()
                                        .onChanged { gesture in
                                            activeId = v.id
                                            dragOffsets[v.id] = gesture.translation
                                        }
                                        .onEnded { gesture in
                                            handleDrop(for: v, translation: gesture.translation)
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
    
    private func handleDrop(for v: VehicleModel, translation: CGSize) {
        if translation.height < -100 {
            SoundManager.shared.playSuccessSound()
            withAnimation(.spring(response: 0.35, dampingFraction: 0.65)) {
                if let idx = vehicles.firstIndex(where: { $0.id == v.id }) {
                    vehicles[idx].isMatched = true
                }
                dragOffsets[v.id] = .zero
                activeId = nil
            }
            if vehicles.allSatisfy({ $0.isMatched }) {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.35) {
                    withAnimation { showCelebration = true }
                }
            }
        } else {
            SoundManager.shared.playErrorSound()
            withAnimation {
                errorVehicleId = v.id
            }
            withAnimation(.spring(response: 0.4, dampingFraction: 0.6)) {
                dragOffsets[v.id] = .zero
                activeId = nil
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.6) {
                withAnimation {
                    if errorVehicleId == v.id {
                        errorVehicleId = nil
                    }
                }
            }
        }
    }
    
    private func loadLevel(_ level: Int) {
        showCelebration = false
        currentLevel = level
        dragOffsets.removeAll()
        activeId = nil
        errorVehicleId = nil
        let list = levelVehicles[(level - 1) % levelVehicles.count]
        activeTypes = Array(Set(list.map { $0.type })).sorted(by: { $0.rawValue < $1.rawValue })
        vehicles = list.shuffled().map { VehicleModel(id: $0.id, name: $0.name, emoji: $0.emoji, type: $0.type, isMatched: false) }
    }
}
