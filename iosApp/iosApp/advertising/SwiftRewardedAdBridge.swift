import GoogleMobileAds
import UIKit
import Shared

final class SwiftRewardedAdBridge: NSObject, IosRewardedAdBridge {

    private var rewardedAd: RewardedAd?

    override init() {
        super.init()
    }

    func loadAd(
        adUnitID: String,
        onSuccess: @escaping () -> Void,
        onError: @escaping (String) -> Void
    ) {
        Task {
            do {
                rewardedAd = try await RewardedAd.load(
                    with: adUnitID,
                    request: Request()
                )
                onSuccess()
            } catch {
                onError(error.localizedDescription)
            }
        }
    }

    func showAd(
        onResult: @escaping (RewardedAdResult) -> Void
    ) {
        guard let rootViewController = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .flatMap({ $0.windows })
            .first(where: { $0.isKeyWindow })?
            .rootViewController,
              let rewardedAd = rewardedAd
        else {
            onResult(.error)
            return
        }

        rewardedAd.present(from: rootViewController) {
            onResult(.rewarded)
        }
    }

    func reset() {
        rewardedAd = nil
    }
}
