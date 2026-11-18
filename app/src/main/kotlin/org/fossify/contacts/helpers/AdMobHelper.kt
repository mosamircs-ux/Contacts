package org.fossify.contacts.helpers

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import org.fossify.contacts.BuildConfig
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

object AdMobHelper {
    private val isInitialized = AtomicBoolean(false)

    fun ensureInitialized(context: Context) {
        if (isInitialized.compareAndSet(false, true)) {
            val appContext = context.applicationContext
            thread(start = true, name = "admob-init") {
                try {
                    MobileAds.initialize(appContext) {}
                } catch (_: Exception) {
                    isInitialized.set(false)
                }
            }
        }
    }

    fun loadAdaptiveBanner(activity: Activity, container: FrameLayout): AdView? {
        val adUnitId = BuildConfig.ADMOB_BANNER_AD_UNIT_ID
        if (adUnitId.isBlank()) {
            container.visibility = View.GONE
            return null
        }

        ensureInitialized(activity)

        val adView = AdView(activity).apply {
            this.adUnitId = adUnitId
            setAdSize(getAdaptiveBannerSize(activity, container))
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    container.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    container.visibility = View.GONE
                }
            }
        }

        container.removeAllViews()
        container.addView(
            adView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        adView.loadAd(AdRequest.Builder().build())
        return adView
    }

    private fun getAdaptiveBannerSize(activity: Activity, container: FrameLayout): AdSize {
        val density = activity.resources.displayMetrics.density
        val adWidthPixels = if (container.width > 0) {
            container.width.toFloat()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.windowManager.currentWindowMetrics.bounds.width().toFloat()
        } else {
            activity.resources.displayMetrics.widthPixels.toFloat()
        }
        val adWidthDp = (adWidthPixels / density).toInt().coerceAtLeast(320)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidthDp)
    }
}
