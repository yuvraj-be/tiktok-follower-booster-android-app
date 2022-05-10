package app.witwork.boosterlike.presentation.explore

import app.witwork.boosterlike.common.base.BaseView
import app.witwork.boosterlike.domain.model.Feed

interface ExploreView : BaseView {
    fun onGetFeedsSuccess(page: Int)
    fun onFollowSuccess()
}