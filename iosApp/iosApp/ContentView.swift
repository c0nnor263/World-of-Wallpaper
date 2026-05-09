import Shared
import SwiftUI
import UIKit

struct ComposeView: UIViewControllerRepresentable {
    private let advertisingBridge = SwiftAdvertisingBridge()

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            advertisingBridge: advertisingBridge
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
