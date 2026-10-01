import SwiftUI

@main
struct ToddlerGamesApp: App {
    var body: some Scene {
        WindowGroup {
            MainMenuView()
                .preferredColorScheme(.light)
        }
    }
}
