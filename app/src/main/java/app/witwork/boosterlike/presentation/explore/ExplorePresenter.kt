package app.witwork.boosterlike.presentation.explore

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.extension.getStringPref
import app.witwork.boosterlike.common.extension.removeKeyPref
import app.witwork.boosterlike.common.utils.SharePref
import app.witwork.boosterlike.domain.model.Feed
import app.witwork.boosterlike.domain.repositoy.FeedRepository
import com.steve.utilities.core.extensions.addToCompositeDisposable
import com.steve.utilities.core.extensions.observableTransformer
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ExplorePresenter @Inject constructor() : BasePresenter<ExploreView>() {
    companion object {
        private const val THREAD_HOLD = 5
        private const val DELAY_LOADING = 0L
    }

    @Inject
    lateinit var feedRepository: FeedRepository

    private var feeds = mutableListOf<Feed>()
    private var followDispose: Disposable? = null
    private val handle = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        view?.showProgressDialog(true)
    }

    var page = 1

    fun loadFeeds(isRefresh: Boolean = false) {
        if (isRefresh) {
            page = 1
        }

        feedRepository.getFeeds(page)
            .compose(observableTransformer())
            .doOnNext { feeds.addAll(it) }
            .subscribe(
                {
                    Timber.i("loadData #success")
                    view?.onGetFeedsSuccess(page++)
                },
                {
                    Timber.i("loadData #error: ${it.message}")
                    view?.showError(it)
                })
            .addToCompositeDisposable(disposable)
    }

    fun getNewFeed(): Feed? {
        val iterator = feeds.iterator()

        if (iterator.hasNext()) {
            val feed = iterator.next()
            iterator.remove()

            if (feeds.size == THREAD_HOLD) {
                loadFeeds()
            }
            Timber.i("getNewFeed: ${feed.name} ; size = ${feeds.size}")
            return feed
        }

        return null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val feedId = getStringPref(SharePref.KEY_FEED_ID)

        if (feedId.isNullOrBlank()) {
            return
        }

        followDispose?.dispose()
        followDispose = Observable
            .fromCallable {
                return@fromCallable feedId
            }
            .flatMap {
                return@flatMap feedRepository.follow(it)
            }
            .flatMap { responseBody ->
                val bodyString = responseBody.string()
                val jsonObject = JSONObject(bodyString)
                val error = jsonObject.getInt("error")
                if (error == 0) {
                    val data = jsonObject.getJSONObject("data")
                    val success = data.getInt("success")
                    removeKeyPref(SharePref.KEY_FEED_ID)
                    if (success == 1) {
                        return@flatMap Observable.just(success)
                    }
                }
                return@flatMap Observable.error<Exception>(Exception("Oops"))
            }
            .compose(observableTransformer())
            .doOnSubscribe { handle.postDelayed(runnable, DELAY_LOADING) }
            .doFinally {
                handle.removeCallbacks(runnable)
                view?.showProgressDialog(false)
            }
            .subscribe({
                view?.onFollowSuccess()
            }, {
                Timber.i("loadData #error: ${it.message}")
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        handle.removeCallbacks(runnable)
        followDispose?.dispose()
    }
}