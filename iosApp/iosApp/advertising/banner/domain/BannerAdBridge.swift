import UIKit
import SwiftUI
import Shared

final class BannerAdBridge: NSObject, IosBannerAdBridge {
    func createBannerView(adUnitId: String) -> UIView {
        return IOSBannerContainerView(adUnitId: adUnitId)
    }
}