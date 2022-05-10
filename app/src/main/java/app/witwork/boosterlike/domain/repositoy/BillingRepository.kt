package app.witwork.boosterlike.domain.repositoy

import android.app.Activity
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import io.reactivex.Observable

interface BillingRepository {
    fun startConnections()

    fun listenerSkuDetailsResult(): Observable<List<SkuDetails>>

    fun listenerPurchase(): Observable<Purchase>

    fun buy(activity: Activity, skuDetails: SkuDetails)

    fun endConnections()
}