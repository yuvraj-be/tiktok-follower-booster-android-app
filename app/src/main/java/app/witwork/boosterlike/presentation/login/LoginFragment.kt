package app.witwork.boosterlike.presentation.login

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.base.BaseFragment
import app.witwork.boosterlike.common.base.BasePresenter
import app.witwork.boosterlike.common.di.component.AppComponent
import app.witwork.boosterlike.common.extension.setLocalStore
import app.witwork.boosterlike.presentation.MainActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginView, LoginPresenter>(), LoginView {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun presenter(): BasePresenter<LoginView>? {
        return presenter
    }

    override fun viewIF(): LoginView? {
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        btnLogin.setOnClickListener {
            run {
                if (idInputLogin.text.isEmpty()) {
                    idErrorLogin.visibility = View.VISIBLE
                } else {
                    idLoginLoading.visibility = View.VISIBLE
                    presenter.login(idInputLogin.text.toString())
                }
            }
        }

        idInputLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    idErrorLogin.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    override fun onLoginSuccess() {
        val login = presenter.getLoginResponse()


        if (login.error?.equals(0)!! && login.message.equals("success")) {
            idLoginLoading.visibility = View.GONE
            MainActivity.isFirstLogin = true
            context?.setLocalStore("APP", "user", Gson().toJson(login), Context.MODE_PRIVATE)
            context?.setLocalStore("APP", "OWNER_USER_ID", login.data?.id!!, Context.MODE_PRIVATE)
            MainActivity.start(context!!)
            requireActivity()?.finish()
        } else {
            idErrorLogin.visibility = View.VISIBLE
            idLoginLoading.visibility = View.GONE
        }
    }

    override fun onLoginFailure() {
        idErrorLogin.visibility = View.VISIBLE
        idLoginLoading.visibility = View.GONE
    }
}
