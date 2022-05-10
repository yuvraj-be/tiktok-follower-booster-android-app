package app.witwork.boosterlike.presentation.boost

import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.domain.model.Boosts
import app.witwork.boosterlike.domain.repositoy.FeedRepository
import com.steve.utilities.core.extensions.addToCompositeDisposable
import com.steve.utilities.core.extensions.observableTransformer
import org.json.JSONObject
import javax.inject.Inject

class BoostPresenter @Inject constructor() : BasePresenter<BoostView>() {
    @Inject
    lateinit var feedRepository: FeedRepository

    var boosts: Boosts = Boosts()

    fun boost(stars: Int, userId: String, boostStarsId: String) {
        feedRepository.boost(stars, userId, boostStarsId)
            .compose(observableTransformer())
            .subscribe ({
                val bodyString = it.string()
                val jsonObject = JSONObject(bodyString)
                val error = jsonObject.getInt("error")
                if (error == 0) {
                    val success = jsonObject.getString("message")
                    if (success == "success") {
                        view?.onBoostSuccess()
                    }
                }
            }, {

            })
            .addToCompositeDisposable(disposable)
    }

    fun getBoost() {
        feedRepository.getBoost()
            .compose(observableTransformer())
            .subscribe ({
               boosts = it
                view?.onGetBoostSuccess()
            }, {

            })
            .addToCompositeDisposable(disposable)
    }
}