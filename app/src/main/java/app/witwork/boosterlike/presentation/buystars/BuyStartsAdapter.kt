package app.witwork.boosterlike.presentation.buystars

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.witwork.boosterlike.R
import app.witwork.boosterlike.domain.model.BoostPackage
import com.steve.utilities.core.extensions.inflate
import kotlinx.android.synthetic.main.item_buy_start.view.*

class BuyStartsAdapter(
    private val boostPackages: MutableList<BoostPackage>,
    private val callback: (BoostPackage) -> Unit
) : RecyclerView.Adapter<BuyStartsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_buy_start)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return boostPackages.count()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            itemView.btnPack.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            callback.invoke(boostPackages[adapterPosition])
        }

        @SuppressLint("SetTextI18n")
        fun bindData() {
            boostPackages[adapterPosition].let {
                itemView.tvPrice.text = it.price
                itemView.tvStar.text = "${it.packageStar} ⭐"
            }
        }
    }

}