package app.witwork.boosterlike.data.repository

import app.witwork.boosterlike.data.remote.AppService
import app.witwork.boosterlike.domain.model.Config
import app.witwork.boosterlike.domain.repositoy.AppRepository
import io.reactivex.Observable
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor() : AppRepository {
    @Inject
    lateinit var appService: AppService

    override fun config(): Observable<Config> {
        return appService.config()
    }

}