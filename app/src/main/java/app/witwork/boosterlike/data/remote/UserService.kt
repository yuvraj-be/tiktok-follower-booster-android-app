package app.witwork.boosterlike.data.remote

import app.witwork.boosterlike.data.model.LoginRequest
import app.witwork.boosterlike.data.model.ProfileRequest
import app.witwork.boosterlike.data.model.PurchaseRequest
import app.witwork.boosterlike.data.model.TokenRequest
import app.witwork.boosterlike.domain.model.Login
import com.android.billingclient.api.Purchase
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("user/purchaseStar")
    fun purchaseStar(@Body purchase: PurchaseRequest): Observable<Purchase>

    @POST("user/login")
    fun login(@Body login: LoginRequest): Observable<Login>

    @POST("user/profile")
    fun profile(@Body profile: ProfileRequest): Observable<Login>

    @POST("reward/video")
    fun rewardVideo(@Body profile: ProfileRequest): Observable<ResponseBody>

    @POST("reward/tiktok")
    fun rewardTiktok(@Body profile: ProfileRequest): Observable<ResponseBody>

    @POST("user/updateToken")
    fun updateToken(@Body tokenRequest: TokenRequest): Observable<Unit>
}