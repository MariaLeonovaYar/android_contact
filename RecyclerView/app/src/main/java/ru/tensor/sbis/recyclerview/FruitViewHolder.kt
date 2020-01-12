package ru.tensor.sbis.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import ru.tensor.sbis.recyclerview.items.FruitListItem

class FruitViewHolder(itemView: View,
                      private val clickHandler: ClickHandler
) : RecyclerView.ViewHolder(itemView) {

    interface ClickHandler {

        fun onFruitViewClick(adapterPosition: Int)
    }

    private val fruitPhoto: SimpleDraweeView = itemView.findViewById(R.id.fruit_photo)
    private val fruitName: TextView = itemView.findViewById(R.id.fruit_name)
    private val fruitQuantity: TextView = itemView.findViewById(R.id.fruit_quantity)

    init {
        itemView.setOnClickListener { clickHandler.onFruitViewClick(adapterPosition) }
    }

    fun bind(fruitListItem: FruitListItem) {
        fruitPhoto.setImageURI(fruitListItem.photoUri)
        fruitName.text = fruitListItem.name
        fruitQuantity.text = fruitListItem.quantity
    }
}