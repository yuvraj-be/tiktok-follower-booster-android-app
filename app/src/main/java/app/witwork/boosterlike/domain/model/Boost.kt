package app.witwork.boosterlike.domain.model
import com.google.gson.annotations.SerializedName


data class Boosts(
    @SerializedName("data")
    var data: List<Boost>? = null,
    @SerializedName("error")
    var error: Int? = null,
    @SerializedName("message")
    var message: String? = null
)

data class Boost(
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("numberOfFollower")
    var numberOfFollower: Int? = null,
    @SerializedName("stars")
    var stars: Int? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null
)