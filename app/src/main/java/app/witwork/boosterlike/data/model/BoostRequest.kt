package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

data class BoostRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("boostStarsId")
    val boostStarsId: String
)