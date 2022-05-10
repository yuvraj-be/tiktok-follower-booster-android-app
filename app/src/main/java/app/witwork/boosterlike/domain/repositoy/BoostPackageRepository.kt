package app.witwork.boosterlike.domain.repositoy

import app.witwork.boosterlike.domain.model.BoostPackage
import io.reactivex.Observable

interface BoostPackageRepository {
    fun getBoostPackages(): Observable<List<BoostPackage>>
}