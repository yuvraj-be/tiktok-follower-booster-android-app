package app.witwork.boosterlike.presentation.buystars

import app.witwork.boosterlike.common.base.BaseView
import app.witwork.boosterlike.domain.model.BoostPackage

interface BuyStarsView : BaseView {
    fun onGetSkuDetailsSuccess(boostPackages: MutableList<BoostPackage>)
    fun onPurchaseSuccess(message: String)
}