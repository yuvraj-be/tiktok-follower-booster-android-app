package app.witwork.boosterlike.domain.repositoy

import app.witwork.boosterlike.data.model.LoginRequest
import app.witwork.boosterlike.data.model.ProfileRequest
import app.witwork.boosterlike.data.model.PurchaseRequest
import app.witwork.boosterlike.domain.model.Login
import com.android.billingclient.api.Purchase
import io.reactivex.Observable
import okhttp3.ResponseBody

interface UserRepository {
    fun syncPurchase(purchase: Purchase, purchaseRequest: PurchaseRequest): Observable<Purchase>
    fun login(loginRequest: LoginRequest): Observable<Login>
    fun profile(profileRequest: ProfileRequest): Observable<Login>
    fun rewardVideo(profileRequest: ProfileRequest): Observable<ResponseBody>
    fun rewardTiktok(profileRequest: ProfileRequest): Observable<ResponseBody>

    fun refreshToken(playId: String?, userId: String?): Observable<Unit>
}