package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

class BoostPackageRequest(@SerializedName("os") val os: String) {
    companion object {
        val default: BoostPackageRequest by lazy {
            return@lazy BoostPackageRequest("Android")
        }
    }

}