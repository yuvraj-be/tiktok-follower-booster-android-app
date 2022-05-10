package app.witwork.boosterlike.common.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import app.witwork.boosterlike.BuildConfig
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.MyApp
import app.witwork.boosterlike.common.base.SubActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import kotlinx.android.synthetic.main.fragment_explore.*
import org.json.JSONObject
import timber.log.Timber
import java.security.MessageDigest
import kotlin.math.roundToInt

fun Context?.openLink(name: String?) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://www.tiktok.com/@$name")
    )
    this?.startActivity(browserIntent)
}

fun Context?.startActivity(fragment: Class<*>, bundle: Bundle? = null) {
    SubActivity.start(this, fragment, bundle)
}

fun Context?.dp2Px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this?.resources?.displayMetrics
    ).roundToInt()
}

fun Any.putStringPref(key: String?, value: String?) {
    MyApp.self.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        .edit()
        .putString(key, value)
        .apply()
}

fun Any.getStringPref(key: String, default: String = ""): String? {
    return MyApp.self.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        .getString(key, default)
}

fun Any.removeKeyPref(key: String) {
    return MyApp.self.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        .edit()
        .remove(key)
        .apply()
}

fun Activity.transparentStatusAndNavigation() {
    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    this.getWindow().setStatusBarColor(Color.TRANSPARENT)
    // this lines ensure only the status-bar to become transparent without affecting the nav-bar
    // this lines ensure only the status-bar to become transparent without affecting the nav-bar
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

//    val window: Window = window
//    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//    window.setStatusBarColor(0x00000000) // transparent
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.bg)
        .centerCrop()
        .addListener(object : RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                Timber.e(e)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .into(this)
}

fun String.hash256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

fun Context.setLocalStore(
    name: String,
    key: String,
    value: String,
    mode: Int = Context.MODE_PRIVATE
) {
    val sharedPreferences = this.getSharedPreferences(name, mode)
    val editor = sharedPreferences?.edit()
    editor?.putString(key, value)
    editor?.apply()
}

fun Context.getLocalStore(name: String, key: String, mode: Int = Context.MODE_PRIVATE): String? {
    return this.getSharedPreferences(name, mode).getString(key, null)
}

fun Context.adjustFontScale(configuration: Configuration) {
    configuration.fontScale = 1.0.toFloat()
    val metrics = resources.displayMetrics
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(metrics)
    metrics.scaledDensity = configuration.fontScale * metrics.density
    this.resources.updateConfiguration(configuration, metrics)
}

fun AdView.setup(){
    val adRequest = AdRequest.Builder().build()
    this.loadAd(adRequest)
}


fun main() {
    val unixTime = System.currentTimeMillis() / 1000L
    val ch = intArrayOf(0x7C, 0x6B, 0x65, 0x79, 0x64, 0x6f, 0x77, 0x6e, 0x21, 0x40, 0x23)
    val key = ch.joinToString(separator = "") { it.toChar().toString() }
    val tmp = "$unixTime$key"
    val hash = tmp.hash256()
    println("tmp: $tmp")
    println("hash: $hash")
}

fun Int.withSuffix(): String? {
    if (this < 1000) return "" + this
    val exp = (Math.log(this.toDouble()) / Math.log(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        this / Math.pow(1000.0, exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}