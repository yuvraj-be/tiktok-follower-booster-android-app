package app.witwork.boosterlike.presentation.profile

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.extension.getStringPref
import app.witwork.boosterlike.common.extension.removeKeyPref
import app.witwork.boosterlike.common.utils.SharePref
import app.witwork.boosterlike.data.model.ProfileRequest
import app.witwork.boosterlike.domain.repositoy.FeedRepository
import app.witwork.boosterlike.domain.repositoy.UserRepository
import com.steve.utilities.core.extensions.addToCompositeDisposable
import com.steve.utilities.core.extensions.observableTransformer
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : BasePresenter<ProfileView>() {
    @Inject
    lateinit var userRepository: UserRepository
    private var followDispose: Disposable? = null

    fun rewardVideo(id: String) {
        userRepository.rewardVideo(ProfileRequest(id))
            .compose(observableTransformer())
            .subscribe {
                view?.onRewardVideoSuccess()
            }
            .addToCompositeDisposable(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        followDispose?.dispose()

        followDispose = Observable
            .fromCallable {
                return@fromCallable getStringPref(SharePref.KEY_USER_ID) ?: ""
            }
            .flatMap {
                return@flatMap userRepository.rewardTiktok(ProfileRequest(it))
            }
            .flatMap { responseBody ->
                val bodyString = responseBody.string()
                val jsonObject = JSONObject(bodyString)
                val error = jsonObject.getInt("error")
                val message = jsonObject.getString("message")
                if (error == 0 && message == "success") {
                    return@flatMap Observable.just(1)
                }
                return@flatMap Observable.error<Exception>(Exception("Oops"))
            }
            .compose(observableTransformer())
            .subscribe({
                removeKeyPref(SharePref.KEY_USER_ID)
                view?.onFollowSuccess()
            }, {
                Timber.i("loadData #error: ${it.message}")
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        followDispose?.dispose()
    }
}
