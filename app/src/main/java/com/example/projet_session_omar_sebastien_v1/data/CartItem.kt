package com.example.projet_session_omar_sebastien_v1.data

data class CartItem(
    val cartItemId: Int,
    val userId: Int,
    val productId: Int,
    val quantity: Int
){
    var id: Int = -1
    constructor(userId: Int, productId: Int, quantity: Int) : this(-1, userId, productId, quantity){
        this.id = id
    }
}
