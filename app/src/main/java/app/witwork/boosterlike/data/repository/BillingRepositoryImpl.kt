package app.witwork.boosterlike.data.repository

import android.app.Activity
import android.content.Context
import app.witwork.boosterlike.common.utils.ProductSku
import app.witwork.boosterlike.domain.repositoy.BillingRepository
import com.android.billingclient.api.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * https://developer.android.com/google/play/billing/integrate
 */
class BillingRepositoryImpl @Inject constructor() : BillingRepository,
    BillingClientStateListener, PurchasesUpdatedListener {
    @Inject
    lateinit var contextApp: Context

    private var playStoreBillingClient: BillingClient? = null
    private val publishSubject = PublishSubject.create<List<SkuDetails>>()
    private val buyPubSubject = PublishSubject.create<Purchase>()

    //------------------------------BillingRepository----------------------------

    override fun startConnections() {
        playStoreBillingClient = BillingClient.newBuilder(contextApp)
            .enablePendingPurchases() // required or app will crash
            .setListener(this)
            .build()
        connect()
    }

    override fun listenerSkuDetailsResult(): Observable<List<SkuDetails>> {
        return publishSubject
    }

    override fun listenerPurchase(): Observable<Purchase> {
        return buyPubSubject
    }

    override fun buy(activity: Activity, skuDetails: SkuDetails) {
        val billingFlowParams = BillingFlowParams
            .newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        playStoreBillingClient?.launchBillingFlow(activity, billingFlowParams)
    }

    override fun endConnections() {
        playStoreBillingClient?.endConnection()
    }

    //------------------------------BillingClientStateListener----------------------------

    override fun onBillingServiceDisconnected() {
        Timber.d("onBillingServiceDisconnected")
        connect()
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Timber.d("onBillingSetupFinished successfully")
                querySkuDetailsAsync()
                getPurchase()
            }
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                //Some apps may choose to make decisions based on this knowledge.
                Timber.d(billingResult.debugMessage)
            }
            else -> {
                //do nothing. Someone else will connect it through retry policy.
                //May choose to send to server though
                Timber.d(billingResult.debugMessage)
            }
        }
    }

    //------------------------------PurchasesUpdatedListener----------------------------

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                // will handle server verification, consumables, and updating the local cache
                purchases?.let {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
            }
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> connect()
            else -> Timber.d(billingResult.debugMessage)
        }
    }

    //------------------------------Private method----------------------------

    private fun connect(): Boolean {
        if (playStoreBillingClient?.isReady == true) {
            return false
        }
        Timber.d("connecting...")
        playStoreBillingClient?.startConnection(this)
        return true
    }

    private fun querySkuDetailsAsync() {
        Timber.d("querySkuDetailsAsync: #called")
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(ProductSku.skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        playStoreBillingClient?.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    skuDetailsList?.let {
                        publishSubject.onNext(it)
                    }
                }
            }
        }
    }

    private fun getPurchase() {
        val purchases = playStoreBillingClient?.queryPurchases(BillingClient.SkuType.INAPP)?.purchasesList
        Timber.i("getPurchase: $purchases")
        purchases?.let {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    /**
     * Processing purchases
     */
    private fun handlePurchase(purchase: Purchase) {
        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build()
        playStoreBillingClient?.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
                buyPubSubject.onNext(purchase)
            }
        }
    }

}