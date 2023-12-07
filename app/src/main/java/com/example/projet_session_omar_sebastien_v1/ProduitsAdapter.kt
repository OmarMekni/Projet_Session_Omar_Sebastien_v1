package com.example.projet_session_omar_sebastien_v1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_session_omar_sebastien_v1.data.Produit

class ProduitsAdapter(var items: MutableList<Produit>, val onClic:(Produit)->Unit): RecyclerView.Adapter<ProduitsAdapter.ProduitViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_produit, parent, false)
        return ProduitViewHolder(itemView, onClic)
    }



    override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {

        val produit = items[position]
        holder.bindProduit(produit)

        // Gestisci il clic sul pulsante "Ajouter"
        holder.btnAjouter.setOnClickListener {
            onClic(produit)
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }



    class ProduitViewHolder(itemView: View, val onClic:(Produit)->Unit): RecyclerView.ViewHolder(itemView) {

        var nomProduit: TextView
        var prix: TextView
        var photo: ImageView
        private var produitCourant : Produit? = null
        var btnAjouter: Button // Aggiungi questa linea per riferimento al pulsante "Ajouter"

        init {
            itemView.setOnClickListener{
                produitCourant?.let(onClic)
            }
            nomProduit = itemView.findViewById(R.id.nomProduit)
            prix = itemView.findViewById(R.id.prix)
            photo = itemView.findViewById(R.id.imgProduit)
            btnAjouter = itemView.findViewById(R.id.btnAjouter) // Inizializza il pulsante "Ajouter"
        }

        fun bindProduit(produit: Produit){
            nomProduit.text = produit.nomProduit
            prix.text = produit.prix
            photo.setImageResource(produit.imgProduit)
        }
    }


}