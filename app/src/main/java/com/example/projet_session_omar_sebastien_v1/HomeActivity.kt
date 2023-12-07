package com.example.projet_session_omar_sebastien_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_session_omar_sebastien_v1.data.Produit
import com.google.android.material.snackbar.Snackbar
import javax.security.auth.callback.Callback


const val PRODUIT = "PRODUIT"
const val NOM_PRODUIT = "NOM_PRODUIT"
const val PRIX_PRODUIT = "PRIX_PRODUIT"
const val IMG_PRODUIT = "IMG_PRODUIT"
const val LISTE_PRODUITS = "LISTE_PRODUITS"

class HomeActivity : AppCompatActivity() {

    lateinit var recyclerViewProduits: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.include))

        recyclerViewProduits = findViewById(R.id.recyclerViewProduits)


        val items = mutableListOf<Produit>(
            Produit("Iphone 15 pro", "1850$", R.drawable.img_1),
            Produit("Samsung S23", "1200$", R.drawable.img_2),
            Produit("Motorola razr", "850$", R.drawable.img_3),
            Produit("Hp EliteBook 860", "945$", R.drawable.img_4),
            Produit("LG UltraWide 34\"", "460$", R.drawable.img_5),
            Produit("Ipad pro", "1850$", R.drawable.img_6),
            Produit("Apple Watch serie 9", "840$", R.drawable.img_7),
        )

        val adapter = ProduitsAdapter(items) { produit ->
            ajouterProduitPanier(produit)
        }


        recyclerViewProduits.layoutManager = LinearLayoutManager(this)
        recyclerViewProduits.adapter = adapter


        // Esegui una chiamata API per ottenere i dati e popolare l'adapter
        //fetchProduitsFromAPI()
    }


    //per creare il menu
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.home_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    //per creare il menu con la mia toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        return true
    }


    //per controllare le selezioni del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deconnecter -> {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                return true
                showConfirmationDialog()
                //showConfirmationSnackbar()
                return true
            }

            R.id.idPanier -> {
                Intent(this, PanierActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter?")
            .setCancelable(false)
            .setPositiveButton("Oui") { _, _ ->
                // Azione di disconnessione qui
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Non") { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }


//    private fun showConfirmationSnackbar() {
//        val snackbar = Snackbar.make(
//            findViewById(android.R.id.content),
//            "Sei sicuro di voler disconnetterti?",
//            Snackbar.LENGTH_LONG
//        )
//        snackbar.setAction("Sì") {
//            // Azione di disconnessione qui
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        snackbar.setActionTextColor(resources.getColor(android.R.color.holo_green_light))
//        snackbar.show()
//    }


    private val ajouterProduitLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val nomProduit = intent?.getStringExtra(NOM_PRODUIT)
                val prixProduit = intent?.getStringExtra(PRIX_PRODUIT)
                val imgProduit = intent?.getIntExtra(IMG_PRODUIT, 0)

                if (nomProduit != null && prixProduit != null && imgProduit != null) {
                    val produit = Produit(nomProduit, prixProduit, imgProduit)
                    ajouterProduitPanier(produit)
                }


            }
        }


    fun ajouterProduitPanier(produit: Produit) {
        // Verifica se i dati sono validi prima di aggiungere al carrello
        if (produit.nomProduit != null && produit.prix != null && produit.imgProduit != 0) {
            val intent = Intent(this, PanierActivity::class.java)
            intent.putExtra(NOM_PRODUIT, produit.nomProduit)
            intent.putExtra(PRIX_PRODUIT, produit.prix)
            intent.putExtra(IMG_PRODUIT, produit.imgProduit)
            ajouterProduitLauncher.launch(intent)
        }
    }
}




    ////////////////////////////////////////////////////////////

//    private fun fetchProduitsFromAPI() {
//        val apiService = RetrofitInstance.create(ApiService::class.java)
//        val call = apiService.getProduits()
//
//        call.enqueue(object : Callback<List<Produit>> {
//            override fun onResponse(call: Call<List<Produit>>, response: Response<List<Produit>>) {
//                if (response.isSuccessful) {
//                    val produits = response.body()
//                    if (produits != null) {
//                        // Aggiungi i dati ottenuti dalla chiamata API all'adapter
//                        adapter.updateData(produits)
//                    }
//                } else {
//                    // Gestisci un errore nella risposta
//                }
//            }
//
//            override fun onFailure(call: Call<List<Produit>>, t: Throwable) {
//                // Gestisci un errore di rete o un errore nella chiamata API
//            }
//        })
//    }