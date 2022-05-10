package app.witwork.boosterlike.presentation.buystars

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.base.BaseFragment
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.di.component.AppComponent
import app.witwork.boosterlike.common.dialog.WitConfirmDialogFragment
import app.witwork.boosterlike.common.extension.setUpAd
import app.witwork.boosterlike.domain.model.BoostPackage
import app.witwork.boosterlike.presentation.MainActivity
import app.witwork.boosterlike.presentation.MainFragment
import app.witwork.boosterlike.presentation.getMainFragment
import kotlinx.android.synthetic.main.fragment_buy_stars.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BuyStarsFragment : BaseFragment<BuyStarsView, BuyStarsPresenter>(), BuyStarsView,
    MainFragment.OnTabChanged, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: BuyStarsPresenter

    private val mAdapter: BuyStartsAdapter by lazy {
        return@lazy BuyStartsAdapter(presenter.boostPackages) {
            presenter.buyStars(activity, it)
        }
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun presenter(): BasePresenter<BuyStarsView>? {
        return presenter
    }

    override fun viewIF(): BuyStarsView? {
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_buy_stars
    }

    override fun initView() {
        toolbar_title.text = getString(R.string.nav_tab_2)
        recyclerView.apply {
            this.adapter = mAdapter
            this.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    context?.let {
                        return@let ContextCompat.getDrawable(it, R.drawable.common_divider_vertical)
                    }?.let {
                        setDrawable(it)
                    }
                })
        }
        swipeRefresh.setOnRefreshListener(this)
    }

    override fun onChange(tabIndex: Int) {

    }

    override fun onGetSkuDetailsSuccess(boostPackages: MutableList<BoostPackage>) {
        swipeRefresh.isRefreshing = false
        mAdapter.notifyDataSetChanged()
    }

    override fun onPurchaseSuccess(message: String) {
        (activity as MainActivity).getMainFragment().getProfile()
        WitConfirmDialogFragment.open(
            fragmentManager = childFragmentManager,
            type = WitConfirmDialogFragment.BOUGHT_START,
            stars = message
        )
    }

    override fun onRefresh() {
        presenter.getBoostPackages()
    }

    override fun onResume() {
        super.onResume()
        updateStars()
    }

    private fun updateStars() {
        val profile = (activity as MainActivity).getMainFragment().profile()

        idMyStars?.let {
            it.text = "${profile?.stars} ⭐"
            it.setOnClickListener {
                run {
                    (activity as MainActivity).getMainFragment().setCurrentTab(3)
                }
            }
        }
    }

    override fun initData() {
        swipeRefresh.isRefreshing = true
        presenter.getBoostPackages()
        MainActivity.appConfig.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUpAd(it)
            }
        })
    }
}
