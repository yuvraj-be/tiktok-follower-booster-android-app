package app.witwork.boosterlike.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.extension.getLocalStore
import app.witwork.boosterlike.presentation.MainActivity
import app.witwork.boosterlike.presentation.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        checkLogin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        handler.postDelayed(runnable, 1200)
    }

    private fun checkLogin() {
        val userString = getLocalStore("APP", "user", MODE_PRIVATE);

        if (userString != null && userString.toString().isNotEmpty()) {
            MainActivity.start(this)
        } else {
            LoginActivity.start(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}