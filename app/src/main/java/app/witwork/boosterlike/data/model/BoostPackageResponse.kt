package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

class BoostPackageResponse {
    @SerializedName("data")
    var data: List<BoostPackageResponse>? = null

    @SerializedName("packageId")
    var packageId: String? = null

    @SerializedName("packageStar")
    var packageStar: String? = null
}

