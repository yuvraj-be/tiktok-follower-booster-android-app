package app.witwork.boosterlike.presentation.boost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.witwork.boosterlike.R
import app.witwork.boosterlike.domain.model.Boost
import app.witwork.boosterlike.presentation.MainActivity
import kotlinx.android.synthetic.main.fragment_boost.*
import timber.log.Timber
import java.util.*

class BoostHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_boost, parent, false)) {
    private var youNextText: TextView = itemView.findViewById(R.id.idSubTitle)
    private var starsText: TextView = itemView.findViewById(R.id.idStars)
    private var numberOfFollowerText: TextView = itemView.findViewById(R.id.idNumberOfFollower)
    private var packUseButton: Button = itemView.findViewById(R.id.idPackUse)
    private var packBuyButton: Button = itemView.findViewById(R.id.idPackBuy)
    private var boostItem: LinearLayout = itemView.findViewById(R.id.idBoostItem)

    fun bind(detail: Boost, boosts: List<Boost>) {
        val profile = MainActivity.user.value?.data

        boostItem.isBuyStar()
        packBuyButton.visibility = View.VISIBLE
        packUseButton.visibility = View.GONE
        numberOfFollowerText.text = detail.numberOfFollower.toString()
        starsText.text = "${detail.stars}⭐️"
        youNextText.text = "ready to boost"
        val lenOfBoosts = boosts.size
        boosts.forEachIndexed { index, boost ->
            if (index == lenOfBoosts - 1 && profile?.stars!! > (boost.stars!! - 1)) {
                boostItem.isUseStar()
                packBuyButton.visibility = View.GONE
                packUseButton.visibility = View.VISIBLE
            } else if(index + 1 < lenOfBoosts) {
                if ((profile?.stars!! >=  detail.stars!! && profile.stars!! < (boosts[index+1].stars!! - 1)) || profile.stars!! > (boosts[index+1].stars!! - 1)) {
                    boostItem.isUseStar()
                    packBuyButton.visibility = View.GONE
                    packUseButton.visibility = View.VISIBLE
                } else {
                    youNeed()
                }
            }
        }
    }

    private fun youNeed() {
        boostItem.isBuyStar()
        packBuyButton.visibility = View.VISIBLE
        packUseButton.visibility = View.GONE
        youNextText.text = "you need"
    }

    private fun LinearLayout.isBuyStar() {
        this.background = resources.getDrawable(R.drawable.lack)
        this.isClickable = false
    }

    private fun LinearLayout.isUseStar() {
        this.background = resources.getDrawable(R.drawable.bg_pack)
        this.isClickable = true
    }
}