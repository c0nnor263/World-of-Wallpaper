import GoogleMobileAds
import UIKit
import Shared

final class AppOpenAdBridge: NSObject, IosAppOpenAdBridge {
    private var appOpenAd: AppOpenAd?
    private var showCallback: ((AdShowResult) -> Void)?

    override init() {
        super.init()
    }

    func hasCachedAd() -> Bool {
        return appOpenAd != nil
    }

    func loadAd(
        adUnitID: String,
        onSuccess: @escaping () -> Void,
        onError: @escaping (String) -> Void
    ) {
        Task {
            do {
                appOpenAd = try await AppOpenAd.load(
                    with: adUnitID,
                    request: Request()
                )
                onSuccess()
            } catch {
                onError(error.localizedDescription)
            }
        }
    }

    func showAd(onResult: @escaping (AdShowResult) -> Void) {
        guard let rootViewController = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .flatMap({ $0.windows })
            .first(where: { $0.isKeyWindow })?
            .rootViewController,
              let appOpenAd = appOpenAd
        else {
            onResult(.error)
            return
        }

        showCallback = onResult
        appOpenAd.fullScreenContentDelegate = self
        appOpenAd.present(from: rootViewController)
    }

    func reset() {
        appOpenAd = nil
    }
}

extension AppOpenAdBridge: FullScreenContentDelegate {
    func adDidDismissFullScreenContent(_ ad: any FullScreenPresentingAd) {
        showCallback?(.dismissed)
        showCallback = nil
    }

    func ad(
        _ ad: any FullScreenPresentingAd,
        didFailToPresentFullScreenContentWithError error: any Error
    ) {
        showCallback?(.error)
        showCallback = nil
    }
}
