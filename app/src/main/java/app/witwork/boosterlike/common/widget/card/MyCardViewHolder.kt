package app.witwork.boosterlike.common.widget.card

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import app.witwork.boosterlike.R
import app.witwork.boosterlike.domain.model.Feed
import kotlinx.android.synthetic.main.my_card_view_holder.view.*
import timber.log.Timber

class MyCardViewHolder(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs), MyCardView.OnDismissListener {
    companion object {
        private const val CARD_SIZE = 3
    }

    interface OnCardViewHolderCallbackListener {
        fun requestNewFeed()
    }

    val feed: Feed?
        get() {
            val topCardView = (0 until childCount)
                .asSequence()
                .map { index -> getChildAt(index) as? MyCardView }
                .filter { it is MyCardView }
                .lastOrNull()

            return topCardView?.feed
        }
    private val itemView: View = View.inflate(context, R.layout.my_card_view_holder, this)

    var onCardVewHolderCallbackListener: OnCardViewHolderCallbackListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        initCardView()
        updateLevel()
    }

    private fun initCardView() {
        (0 until CARD_SIZE).forEach(this::addCard)
    }

    private fun addCard(index: Int) {
        val count = (0 until childCount)
            .asSequence()
            .map { getChildAt(it) as? MyCardView }
            .filter { it is MyCardView }
            .count()

        if (count >= CARD_SIZE) {
            return
        }

        val cardView = MyCardView(context)
            .apply {
                this.layoutParams = itemView.viewAnchor.layoutParams
                this.onDismissListener = this@MyCardViewHolder
            }
        Timber.i("addCard")
        addView(cardView, index)
    }

    private fun insertAndUpdateCard() {
        addCard(0)
        updateLevel()
    }

    private fun updateLevel() {
        (0 until childCount)
            .asSequence()
            .map { index -> getChildAt(index) as? MyCardView }
            .filter { it is MyCardView }
            .forEachIndexed { index, myCardView ->
                myCardView?.level = CARD_SIZE - 1 - index
            }
    }

    fun updateFeedToBottomCard(feed: Feed?) {
        val bottomCard = (0 until childCount)
            .asSequence()
            .map { index -> getChildAt(index) as? MyCardView }
            .filter { it is MyCardView }
            .firstOrNull()

        bottomCard?.feed = feed
    }

    fun updateAllCards(feedCallback: () -> Feed?) {
        (0 until childCount)
            .asSequence()
            .map { index -> getChildAt(childCount - index) as? MyCardView }
            .filter { it is MyCardView }
            .forEach { it?.feed = feedCallback.invoke() }
    }

    fun clearTop() {
        (0 until childCount)
            .asSequence()
            .map { index -> getChildAt(index) as? MyCardView }
            .filter { it is MyCardView }
            .lastOrNull()?.forceDismiss()
    }

    /**
     * MyCardView.OnDismissListener
     */
    override fun onDismiss(view: View) {
        this.removeView(view)
        insertAndUpdateCard()
        onCardVewHolderCallbackListener?.requestNewFeed()
    }

}