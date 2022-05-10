package app.witwork.boosterlike.common.base;

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseView?> : LifecycleObserver {
    lateinit var disposable: CompositeDisposable
    var view: T? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        disposable = CompositeDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        if (::disposable.isInitialized)
            disposable.dispose()
        view = null
    }

}