import SwiftUI
import Shared

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        KoinInitializer.shared.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
