package app.witwork.boosterlike.data.repository

import app.witwork.boosterlike.data.model.LoginRequest
import app.witwork.boosterlike.data.model.ProfileRequest
import app.witwork.boosterlike.data.model.PurchaseRequest
import app.witwork.boosterlike.data.model.TokenRequest
import app.witwork.boosterlike.data.remote.UserService
import app.witwork.boosterlike.domain.model.Login
import app.witwork.boosterlike.domain.repositoy.UserRepository
import com.android.billingclient.api.Purchase
import io.reactivex.Observable
import okhttp3.ResponseBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {
    @Inject
    lateinit var userService: UserService

    override fun syncPurchase(
        purchase: Purchase,
        purchaseRequest: PurchaseRequest
    ): Observable<Purchase> {
        return userService.purchaseStar(purchaseRequest)
            .map {
                return@map purchase
            }
    }

    override fun login(loginRequest: LoginRequest): Observable<Login> {
        return userService.login(loginRequest)
    }

    override fun profile(profileRequest: ProfileRequest): Observable<Login> {
        return userService.profile(profileRequest)
    }

    override fun rewardVideo(profileRequest: ProfileRequest): Observable<ResponseBody> {
        return userService.rewardVideo(profileRequest)
    }

    override fun rewardTiktok(profileRequest: ProfileRequest): Observable<ResponseBody> {
        return userService.rewardTiktok(profileRequest)
    }

    override fun refreshToken(playId: String?, userId: String?): Observable<Unit> {
        return userService.updateToken(TokenRequest(playId, userId))
    }
}