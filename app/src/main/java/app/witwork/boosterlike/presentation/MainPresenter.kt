package app.witwork.boosterlike.presentation

import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.extension.getStringPref
import app.witwork.boosterlike.common.extension.putStringPref
import app.witwork.boosterlike.common.utils.SharePref
import app.witwork.boosterlike.data.model.ProfileRequest
import app.witwork.boosterlike.domain.model.Config
import app.witwork.boosterlike.domain.model.Login
import app.witwork.boosterlike.domain.repositoy.AppRepository
import app.witwork.boosterlike.domain.repositoy.UserRepository
import com.onesignal.OneSignal
import com.steve.utilities.core.extensions.addToCompositeDisposable
import com.steve.utilities.core.extensions.observableTransformer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.NullPointerException
import javax.inject.Inject

class MainPresenter @Inject constructor() : BasePresenter<MainView>() {
    private var profile: Login = Login()
    private var config: Config = Config()

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var appRepository: AppRepository

    fun getProfile(id: String) {
        userRepository.profile(ProfileRequest(id))
            .compose(observableTransformer())
            .subscribe(
                {
                    profile = it
                    putStringPref(SharePref.KEY_USER_ID, profile.data?.id)
                    view?.onGetProfileSuccess()
                }, {

                }
            )
            .addToCompositeDisposable(disposable)
    }

    fun getConfig() {
        appRepository.config()
            .compose(observableTransformer())
            .subscribe({
                config = it
                view?.onGetConfigSuccess()
            }, {

            })
            .addToCompositeDisposable(disposable)
    }

    fun refreshToken() {
        Observable
            .fromCallable {
                val status = OneSignal.getPermissionSubscriptionState()
                val playId: String? = status.subscriptionStatus.userId

                val userId = getStringPref(SharePref.KEY_USER_ID) ?: ""
                return@fromCallable Pair(playId, userId)
            }
            .flatMap { (playId, userId) ->
                if (playId.isNullOrEmpty()) {
                    return@flatMap Observable.just(Unit)
                }
                return@flatMap userRepository.refreshToken(playId, userId)
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                Timber.i("refreshToken #success")
            }, {
                Timber.e(it.localizedMessage)
            })
            .addToCompositeDisposable(disposable)
    }

    fun getProfileResponse(): Login {
        return profile
    }

    fun getConfigResponse(): Config {
        return config
    }
}