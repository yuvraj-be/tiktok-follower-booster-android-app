package app.witwork.boosterlike.common.base

interface BaseView {
    fun showProgressDialog(isShow: Boolean)
    fun showError(throwable: Throwable)
}