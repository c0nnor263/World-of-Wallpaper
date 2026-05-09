import Shared

final class SwiftAdvertisingBridge: NSObject, IosAdvertisingBridge {

    let rewarded: IosRewardedAdBridge
    let banner: IosBannerAdBridge

    override init() {
        self.rewarded = SwiftRewardedAdBridge()
        self.banner = SwiftBannerAdBridge()
        super.init()
    }
}