package app.witwork.boosterlike.presentation.login

import android.annotation.SuppressLint
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.data.model.LoginRequest
import app.witwork.boosterlike.domain.model.Login
import app.witwork.boosterlike.domain.repositoy.UserRepository
import com.onesignal.OneSignal
import com.steve.utilities.core.extensions.addToCompositeDisposable
import com.steve.utilities.core.extensions.observableTransformer
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginView>() {
    private var login: Login = Login()

    @Inject
    lateinit var userRepository: UserRepository

    @SuppressLint("CheckResult")
    fun login(id: String) {
        userRepository.login(LoginRequest(id))
            .compose(observableTransformer())
            .subscribe({
                login = it
                view?.onLoginSuccess()
            }, {
                view?.onLoginFailure()
            })
            .addToCompositeDisposable(disposable)
    }

    fun getLoginResponse(): Login {
        return login
    }
}