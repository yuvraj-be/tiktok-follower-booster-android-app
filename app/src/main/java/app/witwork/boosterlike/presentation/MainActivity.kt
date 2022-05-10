package app.witwork.boosterlike.presentation;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.base.BaseActivity
import app.witwork.boosterlike.common.base.BaseFragment
import app.witwork.boosterlike.common.extension.transparentStatusAndNavigation
import app.witwork.boosterlike.domain.model.Config
import app.witwork.boosterlike.domain.model.Login


class MainActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        var isFirstLogin: Boolean = false
        val appConfig: MutableLiveData<Config> = MutableLiveData()
        val user: MutableLiveData<Login> = MutableLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusAndNavigation();
    }
    override fun injectFragment(): BaseFragment<*, *> {
        return MainFragment()
    }
}


fun MainActivity.getMainFragment(): MainFragment {
    val mainActivity = this
    return mainActivity.currentFragment as MainFragment
}