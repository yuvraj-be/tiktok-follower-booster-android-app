package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    val userName: String
) {
}