package app.witwork.boosterlike.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.extension.loadImage
import app.witwork.boosterlike.common.extension.withSuffix
import app.witwork.boosterlike.domain.model.Data
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.idProfileAvatar
import kotlinx.android.synthetic.main.fragment_profile.idProfileFollowers
import kotlinx.android.synthetic.main.fragment_profile.idProfileFollowing
import kotlinx.android.synthetic.main.fragment_profile.idProfileLikes
import kotlinx.android.synthetic.main.fragment_profile.idProfileName
import kotlinx.android.synthetic.main.wit_dialog_fragment.*
import kotlinx.android.synthetic.main.wit_dialog_fragment.btnClose
import kotlinx.android.synthetic.main.wit_dialog_fragment.btnNegative
import kotlinx.android.synthetic.main.wit_dialog_fragment.btnPositive
import kotlinx.android.synthetic.main.wit_dialog_fragment.imgDescription
import kotlinx.android.synthetic.main.wit_dialog_fragment.tvMessage
import kotlinx.android.synthetic.main.wit_dialog_fragment.tvTitle

class WitConfirmDialogFragment: DialogFragment(), View.OnClickListener {
    var layoutId: Int = R.layout.wit_dialog_fragment
    var profile: Data = Data()
    companion object {
        @IntDef(REWARD_START, BOUGHT_START, FOLLOWERS_COMING, WELCOME)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Type

        const val REWARD_START = 0
        const val BOUGHT_START = 1
        const val FOLLOWERS_COMING = 2
        const val WELCOME = 4

        private const val KEY_TYPE = "KEY_TYPE"
        private const val KEY_STARS = "KEY_STARS"

        fun open(
            tag: String = "WitConfirmDialogFragment",
            fragmentManager: FragmentManager,
            @Type type: Int, stars: String? = null,
            onPositiveClicked: ((DialogFragment) -> Unit)? = null,
            profile: Data = Data(),
            layoutId: Int = R.layout.wit_dialog_fragment
        ) {
            val dialog = WitConfirmDialogFragment()
                .apply {
                    this.layoutId = layoutId
                    this.profile = profile
                    this.onPositiveClicked = onPositiveClicked
                    arguments = Bundle()
                        .apply {
                            putInt(KEY_TYPE, type)
                            putString(KEY_STARS, stars)
                        }
                }
            dialog.show(fragmentManager, tag)
        }
    }

    private var onPositiveClicked: ((DialogFragment) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view.context)
    }

    private fun initView(useContext: Context) {
        val starts = arguments?.getString(KEY_STARS)
        tvStar?.visibility = View.GONE
        when (arguments?.getInt(KEY_TYPE)) {
            REWARD_START -> {
                tvTitle.text = getString(R.string.dialog_title_reward)
                tvMessage.text = getString(R.string.dialog_message_reward)
                imgDescription.setImageResource(R.drawable.gifbox)
                starts?.let {
                    tvStar.text = it
                    tvStar.visibility = View.VISIBLE
                }
                btnNegative.text = getString(R.string.close)
                btnPositive.text = getString(R.string.wallet)
            }
            BOUGHT_START -> {
                tvTitle.text = getString(R.string.dialog_title_bought_star)
                tvMessage.text = getString(R.string.dialog_message_bought_star)
                imgDescription.setImageResource(R.drawable.rainstar)
                starts?.let {
                    tvStar.text = it
                    tvStar.visibility = View.VISIBLE
                }
                btnNegative.text = getString(R.string.close)
                btnPositive.text = getString(R.string.wallet)
            }
            FOLLOWERS_COMING -> {
                tvTitle.text = getString(R.string.followers_are_coming)
                tvMessage.text = getString(R.string.don_t_take_your_eyes)
                imgDescription.setImageResource(R.drawable.followers_are_coming)
                starts?.let {
                    tvStar.visibility = View.GONE
                }
                btnNegative.text = getString(R.string.close)
                btnPositive.text = getString(R.string.profile)
            }
            WELCOME -> {
                starts?.let {
                    tvStar.visibility = View.VISIBLE
                    tvStar.text = it+" ⭐"
                }
                tvTitle.text = getString(R.string.welcome_to_witbooster)
                tvMessage.text = getString(R.string.got_5_stars)
                btnNegative.visibility = View.GONE
                btnPositive.text = getString(R.string.next)

                profile.apply {
                    idProfileAvatar.loadImage(this.covers)
                    idProfileName.text = "@${this.uniqueId}"
                    idProfileFollowers.text = "${this.following?.withSuffix()}"
                    idProfileFollowing.text = "${this.fans?.withSuffix()}"
                    idProfileLikes.text = "${this.heart?.withSuffix()}"
                }
            }
        }

        btnClose.setOnClickListener(this)
        btnNegative.setOnClickListener(this)
        btnPositive.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnClose,
            btnNegative -> dismiss()
            btnPositive -> {
                dismiss()
                onPositiveClicked?.invoke(this)
            }
        }
    }
}