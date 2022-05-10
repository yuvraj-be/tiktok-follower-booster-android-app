package app.witwork.boosterlike.common.extension

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import app.witwork.boosterlike.R
import app.witwork.boosterlike.domain.model.Config
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.AdListener
import timber.log.Timber
fun Fragment.setUpAd(config: Config) {
    val adViewContainer = view?.findViewById<FrameLayout>(R.id.adViewContainer)
    val banner = config.data?.google?.findLast { it.adType == "bottom-ads" }

    val adView = AdView(context)
        .apply {
            adSize = AdSize.SMART_BANNER
            adUnitId = banner?.adId
            setup()
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Timber.i("onAdLoaded")
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    Timber.i("onAdFailedToLoad = $p0")
                }
            }
        }
    adViewContainer?.addView(adView)
}