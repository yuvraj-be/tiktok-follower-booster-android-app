package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

data class FollowRequest(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("feedId")
    val feedId: String
)