package app.witwork.boosterlike.data.repository

import app.witwork.boosterlike.data.remote.BoostPackageService
import app.witwork.boosterlike.domain.model.BoostPackage
import app.witwork.boosterlike.domain.repositoy.BoostPackageRepository
import io.reactivex.Observable
import javax.inject.Inject

class BoostPackageRepositoryImpl @Inject constructor() : BoostPackageRepository {
    @Inject
    lateinit var boostPackageService: BoostPackageService

    override fun getBoostPackages(): Observable<List<BoostPackage>> {
        return boostPackageService.getBoosterPackages()
            .map {
                val result = mutableListOf<BoostPackage>()
                it.data?.mapTo(result) { item ->
                    return@mapTo BoostPackage.fromResponse(item)
                }
                return@map result
            }
    }
}