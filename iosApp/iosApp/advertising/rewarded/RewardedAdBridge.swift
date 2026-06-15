import GoogleMobileAds
import UIKit
import Shared

final class RewardedAdBridge: NSObject, IosRewardedAdBridge {
    private var rewardedAd: RewardedAd?

    override init() {
        super.init()
    }

    func hasCachedAd() -> Bool {
        return rewardedAd != nil
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

    func showAd(onResult: @escaping (AdShowResult) -> Void) {
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
            onResult(.shownOrRewardEarned)
        }
    }

    func reset() {
        rewardedAd = nil
    }
}
