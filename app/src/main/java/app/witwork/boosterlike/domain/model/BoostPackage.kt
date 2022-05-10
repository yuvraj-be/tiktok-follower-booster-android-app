package app.witwork.boosterlike.domain.model

import app.witwork.boosterlike.data.model.BoostPackageResponse

data class BoostPackage(val packageId: String, val packageStar: String) {
    companion object {
        fun fromResponse(boostPackageResponse: BoostPackageResponse?): BoostPackage {
            return BoostPackage(
                packageId = boostPackageResponse?.packageId ?: "",
                packageStar = boostPackageResponse?.packageStar ?: ""
            )
        }
    }

    var price : String? = null
}