package app.witwork.boosterlike.data.model

import app.witwork.boosterlike.common.extension.getStringPref
import app.witwork.boosterlike.common.utils.SharePref
import com.android.billingclient.api.Purchase
import com.google.gson.annotations.SerializedName

data class PurchaseRequest(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("packageName")
    val packageName: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("purchaseTime")
    val purchaseTime: Long,
    @SerializedName("purchaseState")
    val purchaseState: Int,
    @SerializedName("purchaseToken")
    val purchaseToken: String,
    @SerializedName("acknowledged")
    val acknowledged: Boolean
) {
    companion object {
        fun fromPurchase(purchase: Purchase): PurchaseRequest {
            return PurchaseRequest(
                userId = getStringPref(SharePref.KEY_USER_ID),
                orderId = purchase.orderId,
                packageName = purchase.packageName,
                productId = purchase.sku,
                purchaseTime = purchase.purchaseTime,
                purchaseState = purchase.purchaseState,
                purchaseToken = purchase.purchaseToken,
                acknowledged = purchase.isAcknowledged
            )
        }
    }
}