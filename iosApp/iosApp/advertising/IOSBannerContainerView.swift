import UIKit
import GoogleMobileAds

final class IOSBannerContainerView: UIView {
    private let bannerView = BannerView(adSize: AdSizeBanner)

    init(adUnitId: String) {
        super.init(frame: .zero)

        bannerView.translatesAutoresizingMaskIntoConstraints = false
        bannerView.adUnitID = adUnitId

        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let rootViewController = windowScene.windows.first?.rootViewController {
            bannerView.rootViewController = rootViewController
        }

        addSubview(bannerView)

        NSLayoutConstraint.activate([
                                        bannerView.centerXAnchor.constraint(equalTo: centerXAnchor),
                                        bannerView.topAnchor.constraint(equalTo: topAnchor),
                                        bannerView.widthAnchor.constraint(equalToConstant: 320),
                                        bannerView.heightAnchor.constraint(equalToConstant: 50)
                                    ])

        bannerView.load(Request())
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}