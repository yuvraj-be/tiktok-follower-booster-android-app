package app.witwork.boosterlike.data.remote

import app.witwork.boosterlike.domain.model.Config
import io.reactivex.Observable
import retrofit2.http.POST

interface AppService {
    @POST("config")
    fun config(): Observable<Config>
}