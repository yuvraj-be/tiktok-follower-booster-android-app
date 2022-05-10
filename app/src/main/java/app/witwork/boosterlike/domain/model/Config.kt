package app.witwork.boosterlike.domain.model

import com.google.gson.annotations.SerializedName

data class Config(
    @SerializedName("data")
    var data: ConfigData? = null,
    @SerializedName("error")
    var error: Int? = null,
    @SerializedName("message")
    var message: String? = null
)

data class ConfigData(
    @SerializedName("google")
    var google: List<Google>? = null
)

data class Google(
    @SerializedName("active")
    var active: Boolean? = null,
    @SerializedName("adsId")
    var adId: String? = null,
    @SerializedName("adProvider")
    var adProvider: String? = null,
    @SerializedName("adsType")
    var adType: String? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("type")
    var type: String? = null
)