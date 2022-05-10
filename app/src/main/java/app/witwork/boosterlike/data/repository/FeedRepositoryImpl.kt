package app.witwork.boosterlike.data.repository

import android.content.Context
import app.witwork.boosterlike.common.extension.getLocalStore
import app.witwork.boosterlike.data.model.BoostRequest
import app.witwork.boosterlike.data.model.FollowRequest
import app.witwork.boosterlike.data.model.PageRequest
import app.witwork.boosterlike.data.remote.FeedService
import app.witwork.boosterlike.domain.model.Boosts
import app.witwork.boosterlike.domain.model.Feed
import app.witwork.boosterlike.domain.model.Login
import app.witwork.boosterlike.domain.repositoy.FeedRepository
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.ResponseBody
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor() : FeedRepository {

    @Inject
    lateinit var feedService: FeedService

    @Inject
    lateinit var appContext: Context

    override fun getFeeds(page: Int): Observable<List<Feed>> {
        val pageRequest = PageRequest(page = page)
        return feedService.feeds(pageRequest)
            .map {
                val feeds = mutableListOf<Feed>()
                return@map it.data?.mapTo(feeds) { feed ->
                    return@mapTo Feed.fromResponse(feed)
                } ?: listOf<Feed>()
            }
    }

    override fun follow(feedId: String): Observable<ResponseBody> {
        return Observable
            .fromCallable {
                val user = appContext.getLocalStore("APP", "user")

                val userJson: Login? = Gson().fromJson(user, Login::class.java)

                return@fromCallable FollowRequest(userJson?.data?.id, feedId)
            }
            .flatMap {
                return@flatMap feedService.follow(it)
            }

    }

    override fun boost(stars: Int, userId: String, boostStarsId: String): Observable<ResponseBody> {
        val boostRequest = BoostRequest(userId, stars, boostStarsId)

        return feedService.boost(boostRequest)
    }

    override fun getBoost(): Observable<Boosts> {
        return feedService.getBoost()
    }
}