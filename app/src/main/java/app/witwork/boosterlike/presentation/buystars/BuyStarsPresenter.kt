package app.witwork.boosterlike.presentation.buystars

import android.app.Activity
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.utils.ProductSku
import app.witwork.boosterlike.data.model.PurchaseRequest
import app.witwork.boosterlike.domain.model.BoostPackage
import app.witwork.boosterlike.domain.repositoy.BillingRepository
import app.witwork.boosterlike.domain.repositoy.BoostPackageRepository
import app.witwork.boosterlike.domain.repositoy.UserRepository
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.steve.utilities.core.extensions.addToCompositeDisposable
import com.steve.utilities.core.extensions.observableTransformer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class BuyStarsPresenter @Inject constructor() : BasePresenter<BuyStarsView>() {
    @Inject
    lateinit var billingRepository: BillingRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var boostPackageRepository: BoostPackageRepository

    private val skuDetails = mutableListOf<SkuDetails>()
    val boostPackages = mutableListOf<BoostPackage>()

    override fun onCreate() {
        super.onCreate()
        billingRepository
            .listenerPurchase()
            .doOnNext { view?.showProgressDialog(true) }
            .observeOn(Schedulers.io())
            .flatMap(this::syncPurchase)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { view?.showProgressDialog(false) }
            .subscribe(this::handleOnPurchaseSuccess, Timber::e)
            .addToCompositeDisposable(disposable)

        billingRepository
            .listenerSkuDetailsResult()
            .subscribe(this::handleGetSkuDetailsSuccess, Timber::e)
            .addToCompositeDisposable(disposable)

    }

    fun getBoostPackages(){
        boostPackageRepository
            .getBoostPackages()
            .compose(observableTransformer())
            .subscribe(this::handleGetBoostPackagesSuccess, Timber::e)
            .addToCompositeDisposable(disposable)
    }

    private fun handleGetSkuDetailsSuccess(skuDetails: List<SkuDetails>) {
        Timber.e("handleGetSkuDetailsSuccess: $skuDetails")
        if (skuDetails.isEmpty()) return
        this.skuDetails.clear()
        this.skuDetails.addAll(skuDetails.sortedByDescending { it.sku })
        this.boostPackages.forEach { boostPackage ->
            boostPackage.price = skuDetails.find { it.sku == boostPackage.packageId }?.price
        }
        view?.onGetSkuDetailsSuccess(boostPackages)
    }

    private fun syncPurchase(purchases: Purchase): Observable<Purchase> {
        val purchaseRequest = PurchaseRequest.fromPurchase(purchases)
        return userRepository
            .syncPurchase(purchases, purchaseRequest)
    }

    private fun handleOnPurchaseSuccess(purchases: Purchase) {
        Timber.e("handlePurchaseSuccess: $purchases")
        val message = boostPackages.firstOrNull { it.packageId == purchases.sku }?.packageStar
        message?.let {
            view?.onPurchaseSuccess(it)
        }
    }

    private fun handleGetBoostPackagesSuccess(result: List<BoostPackage>) {
        ProductSku.skuList.clear()
        boostPackages.clear()
        boostPackages.addAll(result)
        boostPackages.sortedBy { it.packageStar.toIntOrNull() }
        boostPackages.mapTo(ProductSku.skuList) {
            return@mapTo it.packageId
        }
        billingRepository.startConnections()
    }

    override fun onDestroy() {
        super.onDestroy()
        billingRepository.endConnections()
    }

    fun buyStars(activity: Activity?, boostPackage: BoostPackage) {
        activity ?: return
        skuDetails
            .firstOrNull { boostPackage.packageId == it.sku }
            ?.let { skuDetail ->
                billingRepository.buy(activity, skuDetail)
            }
    }
}

