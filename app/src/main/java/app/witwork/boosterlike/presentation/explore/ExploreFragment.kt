package app.witwork.boosterlike.presentation.explore

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.base.BaseFragment
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.di.component.AppComponent
import app.witwork.boosterlike.common.dialog.WitConfirmDialogFragment
import app.witwork.boosterlike.common.dialog.WitConfirmDialogFragment.Companion.REWARD_START
import app.witwork.boosterlike.common.extension.*
import app.witwork.boosterlike.common.utils.SharePref
import app.witwork.boosterlike.common.widget.CurveDrawable
import app.witwork.boosterlike.common.widget.card.MyCardViewHolder
import app.witwork.boosterlike.domain.model.Config
import app.witwork.boosterlike.domain.model.Data
import app.witwork.boosterlike.presentation.AppViewModel
import app.witwork.boosterlike.presentation.MainActivity
import app.witwork.boosterlike.presentation.MainFragment
import app.witwork.boosterlike.presentation.getMainFragment
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class ExploreFragment : BaseFragment<ExploreView, ExplorePresenter>(), ExploreView,
    MainFragment.OnTabChanged,
    View.OnClickListener,
    MyCardViewHolder.OnCardViewHolderCallbackListener {

    @Inject
    lateinit var presenter: ExplorePresenter

    lateinit var appViewModel: AppViewModel

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun presenter(): BasePresenter<ExploreView>? {
        return presenter
    }

    override fun viewIF(): ExploreView? {
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_explore
    }

    override fun initView() {
        cardContainer.onCardVewHolderCallbackListener = this
        btnClear.setOnClickListener(this)
        btnFollow.setOnClickListener(this)
        swipeRefresh.setOnRefreshListener {
            presenter.loadFeeds(true)
        }

        val useContext = context ?: return
        val curveDrawable = CurveDrawable(
            context.dp2Px(180f),
            ContextCompat.getColor(useContext, R.color.colorBackground)
        )
        container.background = curveDrawable
//        adView.setup()
    }

    override fun initData() {
        presenter.loadFeeds()
        initLiveData()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnFollow -> {
                cardContainer.feed?.let {
                    cardContainer.clearTop()
                    putStringPref(SharePref.KEY_FEED_ID, it._id)
                    context.openLink(cardContainer.feed?.name)
                }
            }

            btnClear -> {
                cardContainer.clearTop()
            }
        }
    }

    /**
     * MainFragment.OnTabChanged
     */
    override fun onChange(tabIndex: Int) {
    }

    /**
     * ExploreView
     */
    override fun onGetFeedsSuccess(page: Int) {
        if (page == 1 && presenter.getNewFeed() == null) {
            cardContainer.visibility = View.GONE
            viewNoData.visibility = View.VISIBLE
            swipeRefresh.apply {
                isEnabled = true
                isRefreshing = false
            }
            return
        }
        cardContainer.visibility = View.VISIBLE
        viewNoData.visibility = View.GONE
        swipeRefresh.apply {
            isEnabled = false
            isRefreshing = false
        }

        if (page == 1) {
            cardContainer.updateAllCards {
                return@updateAllCards presenter.getNewFeed()
            }
        }
    }

    override fun onFollowSuccess() {
        (activity as MainActivity).getMainFragment().getProfile()
        WitConfirmDialogFragment.open(
            fragmentManager = childFragmentManager,
            type = REWARD_START,
            stars = "+01 ⭐",
            onPositiveClicked = {
                (activity as MainActivity).getMainFragment().setCurrentTab(3)
            }
        )
    }

    override fun showError(throwable: Throwable) {
        if (presenter.page == 1) {
            cardContainer.visibility = View.GONE
            viewNoData.visibility = View.VISIBLE
            swipeRefresh.apply {
                isEnabled = true
                isRefreshing = false
            }
        }
    }

    /**
     *MyCardViewHolder.OnCardViewHolderCallbackListener
     */
    override fun requestNewFeed() {
        cardContainer.updateFeedToBottomCard(presenter.getNewFeed())
    }

    private fun updateStars(profile: Data) {
        idMyStars?.let {
            it.text = "${profile?.stars} ⭐"
            it.setOnClickListener {
                run {
                    (activity as MainActivity).getMainFragment().setCurrentTab(3)
                }
            }
        }
    }

    private fun initLiveData() {
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        appViewModel.profile.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                updateStars(it.data!!)
            }
        })

        MainActivity.appConfig.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUpAd(it)
            }
        })
    }
}
