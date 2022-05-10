package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("userId")
    val userId: String
) {}