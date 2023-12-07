package com.example.projet_session_omar_sebastien_v1.data

data class Product(
    val productId: Int,
    val name: String,
    val description: String,
    val price: Double,
    //val productImage: ByteArray
) {
    var id: Int = -1

//    constructor(id: Int, name: String, description: String, price: Double, productImage: ByteArray)
//            : this(name, description, price, productImage) {
//        this.id = id
//    }
}


