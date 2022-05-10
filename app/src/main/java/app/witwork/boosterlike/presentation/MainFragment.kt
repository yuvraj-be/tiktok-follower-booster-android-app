package app.witwork.boosterlike.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.MyApp
import app.witwork.boosterlike.common.base.BaseFragment
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.di.component.AppComponent
import app.witwork.boosterlike.common.dialog.WitConfirmDialogFragment
import app.witwork.boosterlike.common.extension.getLocalStore
import app.witwork.boosterlike.common.widget.bottomnav.BottomNavBar
import app.witwork.boosterlike.domain.model.Data
import app.witwork.boosterlike.domain.model.Login
import app.witwork.boosterlike.presentation.boost.BoostFragment
import app.witwork.boosterlike.presentation.buystars.BuyStarsFragment
import app.witwork.boosterlike.presentation.explore.ExploreFragment
import app.witwork.boosterlike.presentation.profile.ProfileFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_custom.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainFragment : BaseFragment<MainView, MainPresenter>(), MainView {
    private val fragments = listOf(
        ExploreFragment(),
        BuyStarsFragment(),
        BoostFragment(),
        ProfileFragment()
    )

    @Inject
    lateinit var presenter: MainPresenter

    lateinit var appViewModel: AppViewModel

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun presenter(): BasePresenter<MainView>? {
        return presenter
    }

    override fun viewIF(): MainView? {
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        viewPager
            .apply {
                adapter = ViewPagerAdapter(childFragmentManager)
                offscreenPageLimit = fragments.count()
            }

        bottomNavBar.listener = object : BottomNavBar.OnTabChangedListener {
            override fun changed(tabIndex: Int): Boolean {
                viewPager.setCurrentItem(tabIndex, false)
                fragments.forEach { (it as? OnTabChanged)?.onChange(tabIndex) }
                return true
            }

            override fun reSelected(tabIndex: Int) {

            }
        }
    }

    override fun initData() {
        super.initData()
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        getProfile()
        presenter.getConfig()
        presenter.refreshToken()

        appViewModel.profile.observe(viewLifecycleOwner, Observer {
            if (it != null && MainActivity.isFirstLogin && it.data != null) {
                WitConfirmDialogFragment.open(
                    fragmentManager = childFragmentManager,
                    type = WitConfirmDialogFragment.WELCOME,
                    stars = it.data?.stars.toString(),
                    profile = it.data!!,
                    layoutId = R.layout.wit_first_login_dialog_fragment
                )
                MainActivity.isFirstLogin = false
            }
        })
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.count()
        }
    }

    interface OnTabChanged {
        fun onChange(tabIndex: Int)
    }

    fun setCurrentTab(tabIndex: Int = 0) {
        viewPager.setCurrentItem(tabIndex, false)
        bottomNavBar.currentTabSelected = tabIndex
    }

    override fun onGetProfileSuccess() {
        val profile = presenter.getProfileResponse()

        appViewModel.profile.postValue(profile)
        MainActivity.user.value = profile
    }

    override fun onGetConfigSuccess() {
        MainActivity.appConfig.value = presenter.getConfigResponse()
    }

    fun getProfile() {
        val user = context?.getLocalStore("APP", "user")

        val userJson: Login? = Gson().fromJson(user, Login::class.java)
        userJson?.let {

            it.data?.id?.let { it1 -> presenter.getProfile(it1) }
        }
    }

    fun profile(): Data? {
        return presenter.getProfileResponse().data
    }
}
