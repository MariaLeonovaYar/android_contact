package ru.tensor.sbis.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tensor.sbis.recyclerview.items.FruitListItem

private const val FRUIT_LIST_ITEM_VIEW_TYPE = 1

internal class FruitsAdapter(
    private val clickHandler: ClickHandler
) : RecyclerView.Adapter<FruitViewHolder>(),
    FruitViewHolder.ClickHandler {

    interface ClickHandler {

        fun onFruitItemClick(item: FruitListItem)
    }

    override fun onFruitViewClick(adapterPosition: Int) {
        clickHandler.onFruitItemClick(items[adapterPosition])
    }

    private var items: MutableList<FruitListItem> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = FRUIT_LIST_ITEM_VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder =
        if (viewType == FRUIT_LIST_ITEM_VIEW_TYPE) {
            val itemView: View = LayoutInflater.from(parent.context).inflate(
                R.layout.fruit_list_item,
                parent,
                false
            )
            FruitViewHolder(itemView, this)
        } else {
            throw IllegalArgumentException("Unknown viewType $viewType")
        }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItemToFirst(fruitListItem: FruitListItem) {
        items.add(0, fruitListItem)
        notifyItemInserted(0)
    }

    fun removeFirst() {
        if (items.isNotEmpty()) {
            items.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    fun setItems(items: List<FruitListItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemsWithDiff(items: List<FruitListItem>) {
        val diffCallback: DiffUtil.Callback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = this@FruitsAdapter.items.size

            override fun getNewListSize(): Int = items.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@FruitsAdapter.items[oldItemPosition].id == items[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@FruitsAdapter.items[oldItemPosition] == items[newItemPosition]
        }
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}