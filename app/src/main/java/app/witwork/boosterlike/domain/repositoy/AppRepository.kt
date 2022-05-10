package app.witwork.boosterlike.domain.repositoy

import app.witwork.boosterlike.domain.model.Config
import io.reactivex.Observable

interface AppRepository {
    fun config(): Observable<Config>
}