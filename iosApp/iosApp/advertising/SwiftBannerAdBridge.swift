import UIKit
import SwiftUI
import Shared

final class SwiftBannerAdBridge: NSObject, IosBannerAdBridge {
    func createBannerView(adUnitId: String) -> UIView {
        return IOSBannerContainerView(adUnitId: adUnitId)
    }
}