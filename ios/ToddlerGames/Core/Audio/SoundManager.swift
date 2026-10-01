import Foundation
import AVFoundation
import AudioToolbox

final class SoundManager: ObservableObject {
    static let shared = SoundManager()
    
    @Published var isMuted: Bool = false
    
    private init() {
        configureAudioSession()
    }
    
    private func configureAudioSession() {
        do {
            try AVAudioSession.sharedInstance().setCategory(.ambient, mode: .default)
            try AVAudioSession.sharedInstance().setActive(true)
        } catch {
            print("Failed to configure audio session: \(error)")
        }
    }
    
    // MARK: - Haptic Feedback
    func triggerHaptic(type: UINotificationFeedbackGenerator.FeedbackType = .success) {
        let generator = UINotificationFeedbackGenerator()
        generator.prepare()
        generator.notificationOccurred(type)
    }
    
    func triggerImpact(style: UIImpactFeedbackGenerator.FeedbackStyle = .medium) {
        let generator = UIImpactFeedbackGenerator(style: style)
        generator.prepare()
        generator.impactOccurred()
    }
    
    // MARK: - Game Sound Effects
    func playPopSound() {
        guard !isMuted else { return }
        triggerImpact(style: .light)
        AudioServicesPlaySystemSound(1104) // Tink sound
    }
    
    func playSuccessSound() {
        guard !isMuted else { return }
        triggerHaptic(type: .success)
        AudioServicesPlaySystemSound(1025) // Fanfare chime
    }
    
    func playSnapSound() {
        guard !isMuted else { return }
        triggerImpact(style: .heavy)
        AudioServicesPlaySystemSound(1057) // Click / snap
    }
    
    func playErrorSound() {
        guard !isMuted else { return }
        // Rung cảnh báo lỗi mạnh
        triggerHaptic(type: .error)
        // Âm thanh 'Bloop/Error buzz' cảnh báo khi làm sai
        AudioServicesPlaySystemSound(1053)
    }
    
    func playCelebrationFanfare() {
        guard !isMuted else { return }
        triggerHaptic(type: .success)
        AudioServicesPlaySystemSound(1026) // Win chime
    }
}
