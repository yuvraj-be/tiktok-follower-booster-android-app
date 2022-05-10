package app.witwork.boosterlike.data.remote

import app.witwork.boosterlike.data.model.BoostPackageRequest
import app.witwork.boosterlike.data.model.BoostPackageResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface BoostPackageService {
    @POST("getBoostPackages")
    fun getBoosterPackages(@Body boostPackageRequest: BoostPackageRequest = BoostPackageRequest.default): Observable<BoostPackageResponse>
}