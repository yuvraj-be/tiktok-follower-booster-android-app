package app.witwork.boosterlike.data.model

import com.google.gson.annotations.SerializedName

class FeedsResponse {
    @SerializedName("data")
    var data: List<FeedResponse>? = null
}

class FeedResponse {
    @SerializedName("signature")
    var signature: String? = null

    @SerializedName("uniqueId")
    var uniqueId: String? = null

    @SerializedName("_id")
    var _id: String? = null

    @SerializedName("covers")
    var covers: String? = null
}