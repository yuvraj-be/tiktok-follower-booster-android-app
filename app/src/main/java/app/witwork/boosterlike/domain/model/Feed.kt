package app.witwork.boosterlike.domain.model

import app.witwork.boosterlike.data.model.FeedResponse

data class Feed(val _id: String?, val name: String?, val signature: String?, val avatar: String?) {
    companion object {
        fun fromResponse(feedResponse: FeedResponse): Feed {
            return Feed(
                _id = feedResponse._id,
                name = feedResponse.uniqueId,
                signature = feedResponse.signature,
                avatar = feedResponse.covers
            )
        }
    }
}