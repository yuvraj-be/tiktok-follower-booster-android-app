package app.witwork.boosterlike.presentation.boost

import app.witwork.boosterlike.common.base.BaseView
import app.witwork.boosterlike.domain.model.Boost

interface BoostView : BaseView {
    fun onBoostSuccess()
    fun onGetBoostSuccess()
    fun onClickBoost(boost: Boost)
}