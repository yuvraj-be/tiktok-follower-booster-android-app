package app.witwork.boosterlike.presentation.login

import app.witwork.boosterlike.common.base.BaseView

interface LoginView : BaseView {
    fun onLoginSuccess()
    fun onLoginFailure()
}