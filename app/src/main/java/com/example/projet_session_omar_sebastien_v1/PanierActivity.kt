package com.example.projet_session_omar_sebastien_v1


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_session_omar_sebastien_v1.data.Produit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CartManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cart_data", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var cartData: MutableList<Produit>

    init {
        cartData = getCart()
    }

    fun addToCart(produit: Produit) {
        if (produit.nomProduit != null && produit.prix != null && produit.imgProduit != 0) {
            if (!cartData.contains(produit)) {
                cartData.add(produit)
                saveCart()
            }
        }
    }


    fun removeFromCart(position: Int) {
        if (position >= 0 && position < cartData.size) {
            cartData.removeAt(position)
            saveCart()
        }
    }



    fun getCart(): MutableList<Produit> {
        val cartJson = sharedPreferences.getString("cart", null)
        return if (cartJson != null) {
            gson.fromJson(cartJson, object : TypeToken<MutableList<Produit>>() {}.type)
        } else {
            mutableListOf()
        }
    }

    private fun saveCart() {
        val editor = sharedPreferences.edit()
        editor.putString("cart", gson.toJson(cartData))
        editor.apply()
    }

    

    /////////
    // Aggiungi questa funzione per ottenere l'ID dell'utente corrente
    private fun getUserId(): Int {
        // Sostituisci con la logica per ottenere l'ID dell'utente corrente dall'autenticazione
        // In questo esempio, recuperiamo l'ID utente memorizzato nelle SharedPreferences
        return sharedPreferences.getInt("user_id", -1)
    }

}





class PanierActivity : AppCompatActivity() {

    lateinit var recyclerViewPanier: RecyclerView
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panier)

        setSupportActionBar(findViewById(R.id.include))
        // Abilita il pulsante di navigazione (freccia indietro)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Imposta il colore della freccia a bianco
        val upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_ios_24)
        upArrow?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        recyclerViewPanier = findViewById(R.id.recyclerViewPanier)
        cartManager = CartManager(this)


        val nomProduit = intent.getStringExtra(NOM_PRODUIT).toString()
        val prixProduit = intent.getStringExtra(PRIX_PRODUIT).toString()
        val imgProduit = intent.getIntExtra(IMG_PRODUIT, 0)


        val produit = Produit(nomProduit, prixProduit, imgProduit)
        // Aggiungi il prodotto al carrello
        cartManager.addToCart(produit)

        // Ottieni i prodotti dal carrello
        val produits = cartManager.getCart()

        recyclerViewPanier.layoutManager = LinearLayoutManager(this)
        val adapter = PanierAdapter(produits)
        recyclerViewPanier.adapter = adapter


        // Aggiungi un listener al pulsante "Retirer"
        adapter.onRetirerClickListener = object : PanierAdapter.OnRetirerClickListener {
            override fun onRetirerClick(position: Int) {

                // Rimuovi il prodotto dal carrello quando il pulsante Retirer viene premuto
                cartManager.removeFromCart(position)

                // Chiama la funzione removeItem dell'adapter per aggiornare la vista
                adapter.removeItem(position)
            }
        }
    }

    //per creare il menu con la mia toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val idPanierItem = menu?.findItem(R.id.idPanier)
        if (idPanierItem != null) {
            idPanierItem.isVisible = false // Nascondi l'icona del carrello
        }
        return super.onPrepareOptionsMenu(menu)
    }

    //per controllare le selezioni del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deconnecter -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}






