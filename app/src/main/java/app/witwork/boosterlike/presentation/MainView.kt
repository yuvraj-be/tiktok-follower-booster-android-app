package app.witwork.boosterlike.presentation

import app.witwork.boosterlike.common.base.BaseView

interface MainView : BaseView {
    fun onGetProfileSuccess()
    fun onGetConfigSuccess()
}