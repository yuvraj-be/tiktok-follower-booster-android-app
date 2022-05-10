package app.witwork.boosterlike.presentation.boost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.witwork.boosterlike.domain.model.Boost

class BoostAdapter(
    private val cellClick: BoostView
) : RecyclerView.Adapter<BoostHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Boost>() {
        override fun areItemsTheSame(
            oldItem: Boost,
            newItem: Boost
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Boost,
            newItem: Boost
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoostHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BoostHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: BoostHolder, position: Int) {
        val boost: Boost? = differ.currentList.get(position)
        val list = differ.currentList.toList()

        boost?.apply {
            holder.bind(this, list)

            holder.itemView.setOnClickListener{run {
                cellClick.onClickBoost(boost)
            }}
        }
    }

    override fun getItemCount(): Int  = differ.currentList.size.let {
        if (it === null) return 0;

        return it;
    }
}