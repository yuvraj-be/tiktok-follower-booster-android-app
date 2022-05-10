package app.witwork.boosterlike.common;

import android.app.Application
import app.witwork.boosterlike.BuildConfig
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.di.component.AppComponent
import app.witwork.boosterlike.common.di.component.DaggerAppComponent
import com.onesignal.OneSignal
import timber.log.Timber

class MyApp : Application() {
    companion object {
        lateinit var self: MyApp
            private set
    }

    val appComponent: AppComponent by lazy {
        return@lazy DaggerAppComponent.builder()
            .application(this)
            .baseUrl(getString(R.string.base_url))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        self = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();
    }
}