package app.witwork.boosterlike.presentation.boost

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.base.BaseFragment
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.di.component.AppComponent
import app.witwork.boosterlike.common.dialog.WitConfirmDialogFragment
import app.witwork.boosterlike.common.extension.setUpAd
import app.witwork.boosterlike.domain.model.Boost
import app.witwork.boosterlike.domain.model.Data
import app.witwork.boosterlike.presentation.AppViewModel
import app.witwork.boosterlike.presentation.MainActivity
import app.witwork.boosterlike.presentation.getMainFragment
import kotlinx.android.synthetic.main.fragment_boost.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BoostFragment : BaseFragment<BoostView, BoostPresenter>(), BoostView {

    @Inject
    lateinit var presenter: BoostPresenter

    lateinit var appViewModel: AppViewModel
    private lateinit var boostAdapter: BoostAdapter

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun presenter(): BasePresenter<BoostView>? {
        return presenter
    }

    override fun viewIF(): BoostView? {
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_boost
    }

    override fun initView() {
        toolbar_title.text = getString(R.string.nav_tab_3)
        pullToRefresh()
        boostAdapter = BoostAdapter(this)
        idListBoosts?.apply {
            adapter = boostAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun initData() {
        super.initData()
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        appViewModel.profile.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showStars(it.data)
                presenter.getBoost()
            }
        })

        MainActivity.appConfig.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUpAd(it)
            }
        })
    }

    private fun pullToRefresh() {
        idSwipeRefreshBoost.setOnRefreshListener {
            (activity as MainActivity).getMainFragment().getProfile()
            idSwipeRefreshBoost.isRefreshing = false
        }
    }

    private fun showStars(userInfo: Data?) {
        if (userInfo == null) return
        idMyStars?.let {
            it.text = "${userInfo?.stars} ⭐"
            it.setOnClickListener {
                run {
                    (activity as MainActivity).getMainFragment().setCurrentTab(3)
                }
            }
        }
    }

    override fun onBoostSuccess() {
        (activity as MainActivity).getMainFragment().getProfile()
        showProgressDialog(false)
        WitConfirmDialogFragment.open(
            fragmentManager = childFragmentManager,
            type = WitConfirmDialogFragment.FOLLOWERS_COMING,
            stars = "",
            onPositiveClicked = {
                (activity as MainActivity).getMainFragment().setCurrentTab(3)
            }
        )
    }

    override fun onClickBoost(boost: Boost) {
        val userInfo = MainActivity.user.value?.data
        if (userInfo?.stars!! >= boost.stars!!) {
            showProgressDialog(true)
            presenter.boost(boost.stars!!, userInfo.id!!, boost.id!!)
        } else {
            (activity as MainActivity).getMainFragment().setCurrentTab(1)
        }
    }

    override fun onGetBoostSuccess() {
        idListBoosts?.apply {
            (adapter as BoostAdapter).differ.submitList(presenter.boosts.data?.sortedBy { it.stars }
                ?.toList())
            adapter?.notifyDataSetChanged()
        }
    }
}
