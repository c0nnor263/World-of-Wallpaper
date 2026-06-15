import Shared

final class AdvertisingBridge: NSObject, IosAdvertisingBridge {

    let rewarded: IosRewardedAdBridge
    let banner: IosBannerAdBridge
    let appOpen: IosAppOpenAdBridge

    override init() {
        self.rewarded = RewardedAdBridge()
        self.banner = BannerAdBridge()
        self.appOpen = AppOpenAdBridge()
        super.init()
    }
}