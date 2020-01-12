package ru.tensor.sbis.recyclerview.items

class FruitListItem(
    val id: String,
    val name: String,
    val quantity: String,
    val phone: String,
    val photoUri: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FruitListItem
        if (name != other.name) return false
        if (quantity != other.quantity) return false
        if (phone != other.phone) return false
        if (photoUri != other.photoUri) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + quantity.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + photoUri.hashCode()
        return result
    }
}