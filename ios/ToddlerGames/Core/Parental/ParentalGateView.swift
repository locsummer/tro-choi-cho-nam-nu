import SwiftUI

struct ParentalGateView: View {
    @Environment(\.dismiss) private var dismiss
    let onSuccess: () -> Void
    
    @State private var num1: Int = Int.random(in: 2...8)
    @State private var num2: Int = Int.random(in: 2...8)
    @State private var userAnswer: String = ""
    @State private var showError: Bool = false
    
    var correctAnswer: Int { num1 * num2 }
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.4)
                .ignoresSafeArea()
            
            VStack(spacing: 20) {
                HStack {
                    Image(systemName: "lock.shield.fill")
                        .foregroundColor(.orange)
                        .font(.title)
                    Text("Dành cho phụ huynh")
                        .font(.system(size: 22, weight: .bold, design: .rounded))
                        .foregroundColor(.primary)
                }
                
                Text("Vui lòng giải phép tính sau để tiếp tục:")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                
                Text("\(num1)  ×  \(num2)  =  ?")
                    .font(.system(size: 36, weight: .heavy, design: .rounded))
                    .foregroundColor(.purple)
                    .padding(.vertical, 8)
                
                // Keypad input display
                Text(userAnswer.isEmpty ? "___" : userAnswer)
                    .font(.system(size: 28, weight: .bold, design: .monospaced))
                    .padding(.horizontal, 24)
                    .padding(.vertical, 8)
                    .background(Color.gray.opacity(0.15))
                    .cornerRadius(12)
                
                if showError {
                    Text("Chưa chính xác, hãy thử lại nhé!")
                        .font(.footnote)
                        .foregroundColor(.red)
                }
                
                // Numeric keypad
                LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 10), count: 3), spacing: 10) {
                    ForEach(1...9, id: \.self) { digit in
                        Button(action: { appendDigit("\(digit)") }) {
                            Text("\(digit)")
                                .font(.title2.bold())
                                .frame(maxWidth: .infinity, minHeight: 48)
                                .background(Color.blue.opacity(0.12))
                                .foregroundColor(.blue)
                                .cornerRadius(12)
                        }
                    }
                    
                    Button(action: { clear() }) {
                        Image(systemName: "delete.left.fill")
                            .font(.title3.bold())
                            .frame(maxWidth: .infinity, minHeight: 48)
                            .background(Color.red.opacity(0.12))
                            .foregroundColor(.red)
                            .cornerRadius(12)
                    }
                    
                    Button(action: { appendDigit("0") }) {
                        Text("0")
                            .font(.title2.bold())
                            .frame(maxWidth: .infinity, minHeight: 48)
                            .background(Color.blue.opacity(0.12))
                            .foregroundColor(.blue)
                            .cornerRadius(12)
                    }
                    
                    Button(action: { checkAnswer() }) {
                        Image(systemName: "checkmark.circle.fill")
                            .font(.title2.bold())
                            .frame(maxWidth: .infinity, minHeight: 48)
                            .background(Color.green)
                            .foregroundColor(.white)
                            .cornerRadius(12)
                    }
                }
                .padding(.horizontal, 10)
                
                Button("Hủy bỏ") {
                    dismiss()
                }
                .foregroundColor(.secondary)
                .padding(.top, 5)
            }
            .padding(24)
            .background(Color(UIColor.systemBackground))
            .cornerRadius(24)
            .shadow(color: .black.opacity(0.2), radius: 20, x: 0, y: 10)
            .frame(maxWidth: 340)
        }
    }
    
    private func appendDigit(_ digit: String) {
        if userAnswer.count < 3 {
            userAnswer.append(digit)
            showError = false
        }
    }
    
    private func clear() {
        if !userAnswer.isEmpty {
            userAnswer.removeLast()
        }
    }
    
    private func checkAnswer() {
        if let val = Int(userAnswer), val == correctAnswer {
            SoundManager.shared.playSuccessSound()
            dismiss()
            onSuccess()
        } else {
            SoundManager.shared.playErrorSound()
            showError = true
            userAnswer = ""
            num1 = Int.random(in: 2...9)
            num2 = Int.random(in: 2...9)
        }
    }
}
