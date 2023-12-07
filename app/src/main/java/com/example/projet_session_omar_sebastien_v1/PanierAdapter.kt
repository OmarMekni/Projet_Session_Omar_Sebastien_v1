package com.example.projet_session_omar_sebastien_v1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_session_omar_sebastien_v1.data.Produit


class PanierAdapter(
    var listeProduitPanier: MutableList<Produit>) : RecyclerView.Adapter<PanierAdapter.PanierViewHolder>() {

    // Interfaccia per il listener del pulsante "Retirer"
    interface OnRetirerClickListener {
        fun onRetirerClick(position: Int)
    }

    // Variabile membro per il listener
    var onRetirerClickListener: OnRetirerClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanierViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_panier, parent, false)
        return PanierViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PanierViewHolder, position: Int) {
        val produitPanier = listeProduitPanier[position]
        holder.lier(produitPanier)

        holder.retirer.setOnClickListener {
            // Chiamare il listener quando il pulsante Retirer viene premuto
            onRetirerClickListener?.onRetirerClick(position)
        }
    }

    override fun getItemCount(): Int {
        return listeProduitPanier.size
    }

    class PanierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomProduitPanier: TextView = itemView.findViewById(R.id.nomProduitPanier)
        var prixProduitPanier: TextView = itemView.findViewById(R.id.prixProduitPanier)
        var photoProduitPanier: ImageView = itemView.findViewById(R.id.imgProduitPanier)
        var retirer: Button = itemView.findViewById(R.id.btnRetirer)

        fun lier(produit: Produit) {
            nomProduitPanier.text = produit.nomProduit
            prixProduitPanier.text = produit.prix
            photoProduitPanier.setImageResource(produit.imgProduit)
        }
    }

    // Aggiungi questa funzione per rimuovere un elemento dalla lista
    fun removeItem(position: Int) {
        if (position >= 0 && position < listeProduitPanier.size) {
            listeProduitPanier.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}

