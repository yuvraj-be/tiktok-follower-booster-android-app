package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token") var token: String?,
    @SerializedName("userId") var userId: String?
)
