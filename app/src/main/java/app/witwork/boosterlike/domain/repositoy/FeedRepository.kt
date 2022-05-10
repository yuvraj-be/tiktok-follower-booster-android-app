package app.witwork.boosterlike.domain.repositoy

import app.witwork.boosterlike.domain.model.Boosts
import app.witwork.boosterlike.domain.model.Feed
import io.reactivex.Observable
import okhttp3.ResponseBody

interface FeedRepository {
    fun getFeeds(page: Int): Observable<List<Feed>>

    fun follow(feedId: String): Observable<ResponseBody>
    fun boost(stars: Int, userId: String, boostStarsId: String): Observable<ResponseBody>
    fun getBoost(): Observable<Boosts>
}