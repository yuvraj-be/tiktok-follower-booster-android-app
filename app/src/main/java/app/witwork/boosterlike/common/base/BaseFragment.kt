package app.witwork.boosterlike.common.base;

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.MyApp
import app.witwork.boosterlike.common.di.component.AppComponent


abstract class BaseFragment<V : BaseView?, P : BasePresenter<V>?> : Fragment(),
    BaseView {
    private val idLayoutRes: Int
        get() {
            return getLayoutRes()
        }

    private val basePresenter: BasePresenter<V>?
        get() {
            return presenter()
        }

    private val dialog: Dialog by lazy {
        val dialog = Dialog(context!!, R.style.LoadingDialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_loading)
//        val window = dialog.window
//        val wlp = window!!.attributes
//        wlp.gravity = Gravity.CENTER
//        window.attributes = wlp
//        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        return@lazy dialog
    }

    abstract fun inject(appComponent: AppComponent)
    abstract fun getLayoutRes(): Int
    abstract fun initView()
    open fun initData() {}
    abstract fun presenter(): BasePresenter<V>?
    abstract fun viewIF(): V?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(MyApp.self.appComponent)
        initPresenter()
    }

    private fun initPresenter() {
        basePresenter?.let {
            it.view = viewIF()
            lifecycle.addObserver(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(idLayoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun showProgressDialog(isShow: Boolean) {
        if (isShow && !dialog.isShowing) {
            dialog.show()
            return
        }
        if (!isShow && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    override fun showError(throwable: Throwable) {

    }

    fun onBackPressed(): Boolean {
        return false
    }
}