package app.witwork.boosterlike.common

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.steve.utilities.core.helper.UnsafeOkHttpClient
import timber.log.Timber
import java.io.InputStream


@GlideModule
class UnsafeOkHttpGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        Timber.i("=============> hihi")
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }
}