package com.example.projet_session_omar_sebastien_v1.db


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.Telephony.Carriers.PASSWORD
import com.example.projet_session_omar_sebastien_v1.data.Product
import com.example.projet_session_omar_sebastien_v1.data.User


class AppDatabase(
    var mContext: Context,
    var name: String = DB_NAME,
    var version: Int = DB_VERSION
): SQLiteOpenHelper(
    mContext,
    DB_NAME,
    null,
    DB_VERSION
) {



    override fun onCreate(db: SQLiteDatabase?) {

        //creation des tables //////////
        val createTableUser = """
           CREATE TABLE users(
              $USER_ID integer PRIMARY KEY,
              $NAME varchar(50),
              $EMAIL varchar(100),
              $PASSWORD varchar(20)
           )           
        """.trimIndent()
        db?.execSQL(createTableUser)


        //table pour le panier
        val createTableCart = """
            CREATE TABLE cart(
                $CART_ID INTEGER PRIMARY KEY,
                $USER_ID INTEGER,
                $PRODUCT_ID INTEGER,
                FOREIGN KEY($USER_ID) REFERENCES users($USER_ID),
                FOREIGN KEY($PRODUCT_ID) REFERENCES products($PRODUCT_ID)
            )
        """.trimIndent()
        db?.execSQL(createTableCart)






    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        //supression des anciens table, et la re creation des nouveaux
        db?.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        onCreate(db)
    }

    fun addUser(user: User): Boolean{

        //inserer un nouveau utilisateur dans la base de données
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(NAME, user.name)
        values.put(EMAIL, user.email)
        values.put(PASSWORD, user.password)

        //insert into users(nom,email,password) values(user.nom,user.email,user.password
        val resultat = db.insert(USERS_TABLE_NAME, null, values).toInt()

        db.close()
        return resultat != -1
    }

    fun findUser(email: String, password: String): User?{

        //puo essere null
        var user: User? = null
        val db = this.readableDatabase
        //val selectQuery = "SELECT * FROM $USERS_TABLE_NAME WHERE $EMAIL=? and $PASSWORD=?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(USERS_TABLE_NAME, null, "$EMAIL=? AND $PASSWORD=?", selectionArgs, null, null, null)

        if (cursor != null){
            if (cursor.moveToFirst()){
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val email = cursor.getString(2)
                //val password = cursor.getString(3)
                val user = User(id, name, email, "")
                return user
            }
        }

        db.close()
        return user
    }

    /////   PRODUCT ///////////////////////////////////

    // Dentro la classe AppDatabase o in una classe dedicata per la gestione dei prodotti

    fun addProductByAdmin(product: Product): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PRODUCT_NAME, product.name)
        values.put(PRODUCT_DESCRIPTION, product.description)
        values.put(PRODUCT_PRICE, product.price)

        val result = db.insert(PRODUCTS_TABLE_NAME, null, values).toInt()
        db.close()
        return result != -1
    }







    /////   panier ///////////////////////////////////

    // Quando un nuovo utente si registra, crea un carrello vuoto per loro
    fun createCartForUser(userId: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USER_ID, userId)
        val result = db.insert(CART_TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }



    fun addToCart(userId: Int, productId: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USER_ID, userId)
        values.put(PRODUCT_ID, productId)
        val result = db.insert(CART_TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }



    fun getCartItems(userId: Int): List<Product> {
        val db = this.readableDatabase
        val query = """
            SELECT $PRODUCT_ID, $PRODUCT_NAME, $PRODUCT_DESCRIPTION, $PRODUCT_PRICE
            FROM $PRODUCTS_TABLE_NAME p
            INNER JOIN $CART_TABLE_NAME c ON p.$PRODUCT_ID = c.$PRODUCT_ID
            WHERE c.$USER_ID = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val products = mutableListOf<Product>()

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val productIdIndex = cursor.getColumnIndex(PRODUCT_ID)
                    val productNameIndex = cursor.getColumnIndex(PRODUCT_NAME)
                    val productDescriptionIndex = cursor.getColumnIndex(PRODUCT_DESCRIPTION)
                    val productPriceIndex = cursor.getColumnIndex(PRODUCT_PRICE)

                    if (productIdIndex != -1 && productNameIndex != -1 && productDescriptionIndex != -1 && productPriceIndex != -1) {
                        val productId = cursor.getInt(productIdIndex)
                        val productName = cursor.getString(productNameIndex)
                        val productDescription = cursor.getString(productDescriptionIndex)
                        val productPrice = cursor.getDouble(productPriceIndex)
                        val product = Product(productId, productName, productDescription, productPrice)
                        products.add(product)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        db.close()
        return products
    }






    fun removeFromCart(userId: Int, productId: Int): Boolean {
        val db = this.writableDatabase
        val whereClause = "$USER_ID = ? AND $PRODUCT_ID = ?"
        val whereArgs = arrayOf(userId.toString(), productId.toString())
        val result = db.delete(CART_TABLE_NAME, whereClause, whereArgs)
        db.close()
        return result > 0
    }





//    //ATTRIBUTI DELLA CLASSE
//    companion object{
//        private val DB_NAME = "app_db"
//        private val DB_VERSION = 1
//        private val USERS_TABLE_NAME = "users"
//        private val USER_ID = "id"
//        private val NAME = "name"
//        private val EMAIL = "email"
//        private val PASSWORD = "password"
//
//
//
//    }


//    // Definisci le costanti per i nomi delle colonne e delle tabelle
//    companion object {
//        private val DB_NAME = "app_db"
//        private val DB_VERSION = 1
//        private val USERS_TABLE_NAME = "users"
//        private val CART_TABLE_NAME = "cart"  // Aggiungi il nome della tabella del carrello
//        private val USER_ID = "id"
//        private val NAME = "name"
//        private val EMAIL = "email"
//        private val PASSWORD = "password"
//        private val CART_ID = "cart_id"  // Aggiungi l'ID del carrello
//        private val PRODUCT_ID = "product_id"  // Aggiungi l'ID del prodotto
//    }





    // Definisci le costanti per i nomi delle colonne e delle tabelle
    companion object {
        private val DB_NAME = "app_db"
        private val DB_VERSION = 1
        private val USERS_TABLE_NAME = "users"
        private val CART_TABLE_NAME = "cart"  // Nome della tabella del carrello
        private val PRODUCTS_TABLE_NAME = "products"  // Nome della tabella dei prodotti
        private val USER_ID = "id"
        private val NAME = "name"
        private val EMAIL = "email"
        private val PASSWORD = "password"
        private val CART_ID = "cart_id"  // ID del carrello
        private val PRODUCT_ID = "product_id"  // ID del prodotto
        private val PRODUCT_NAME = "product_name"  // Nome del prodotto
        private val PRODUCT_DESCRIPTION = "product_description"  // Descrizione del prodotto
        private val PRODUCT_PRICE = "product_price"  // Prezzo del prodotto
    }



}