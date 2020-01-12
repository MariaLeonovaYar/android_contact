package ru.tensor.sbis.recyclerview.items

import java.util.*
import kotlin.random.Random

object Fruits {

    val items: List<FruitListItem> = listOf()

    fun newFruitListItem(
        name: String,
        quantity: String,
        phone: String,
        previewUri: String
    ): FruitListItem =
        FruitListItem(UUID.randomUUID().toString(), name, quantity, phone, previewUri)

    fun randomFruitListItem(): FruitListItem =
        items[Random.nextInt(items.size)].let { fruitListItem ->
            newFruitListItem(
                fruitListItem.name,
                fruitListItem.quantity,
                fruitListItem.phone,
                fruitListItem.photoUri
            )
        }

}