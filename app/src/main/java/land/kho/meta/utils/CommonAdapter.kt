package land.kho.meta.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import land.kho.meta.BR

class CommonAdapter<T : AdapterViewModel?>(
    var items: List<T>,
    private val itemClickListener: ItemClickListener<T>
) :
    RecyclerView.Adapter<CommonAdapter.ViewHolder<T>?>() {

    private var mLayoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(mLayoutInflater!!.inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position]?.viewType!!
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder<T> internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
        fun bind(value: T, clickListener: ItemClickListener<T>?) {
            binding.setVariable(BR.viewModel, value)
            if (clickListener != null) {
                binding.root.setOnClickListener {
                    clickListener.onItemClick(value)
                }
            }
        }

    }

}

