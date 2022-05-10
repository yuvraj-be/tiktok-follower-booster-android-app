package app.witwork.boosterlike.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.extension.adjustFontScale
import app.witwork.boosterlike.common.extension.transparentStatusAndNavigation


class LoginActivity : AppCompatActivity() {
    lateinit var fragmentManager: FragmentManager
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        transparentStatusAndNavigation();

        initView()
        adjustFontScale(resources.configuration)
    }

    private fun initView() {
        fragmentManager = supportFragmentManager;

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        with(fragmentTransaction) {
            add(R.id.fragmentLogin, LoginFragment(), null)
            commit()
        }
    }
}