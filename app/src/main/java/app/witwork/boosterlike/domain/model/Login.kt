package app.witwork.boosterlike.domain.model

import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("data")
    var data: Data? = null,
    @SerializedName("error")
    var error: Int? = null,
    @SerializedName("message")
    var message: String? = null
)

data class Data(
    @SerializedName("covers")
    var covers: String? = null,
    @SerializedName("fans")
    var fans: Int? = null,
    @SerializedName("following")
    var following: Int? = null,
    @SerializedName("heart")
    var heart: Int? = null,
    @SerializedName("nickname")
    var nickname: String? = null,
    @SerializedName("signature")
    var signature: String? = null,
    @SerializedName("tiktokId")
    var tiktokId: String? = null,
    @SerializedName("uniqueId")
    var uniqueId: String? = null,
    @SerializedName("verified")
    var verified: Boolean? = null,
    @SerializedName("video")
    var video: Int? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("stars")
    var stars: Int? = 0
)
