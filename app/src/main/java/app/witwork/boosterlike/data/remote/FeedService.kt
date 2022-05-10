package app.witwork.boosterlike.data.remote

import app.witwork.boosterlike.data.model.BoostRequest
import app.witwork.boosterlike.data.model.FeedsResponse
import app.witwork.boosterlike.data.model.FollowRequest
import app.witwork.boosterlike.data.model.PageRequest
import app.witwork.boosterlike.domain.model.Boosts
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface FeedService {
    @POST("feeds/get")
    fun feeds(@Body pageRequest: PageRequest = PageRequest.default): Observable<FeedsResponse>

    @POST("feeds/follow")
    fun follow(@Body followRequest: FollowRequest): Observable<ResponseBody>

    @POST("feed/boost")
    fun boost(@Body boostRequest: BoostRequest): Observable<ResponseBody>

    @POST("feed/getAllBoost")
    fun getBoost(): Observable<Boosts>
}