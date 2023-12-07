package com.example.projet_session_omar_sebastien_v1.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Produit(
    var nomProduit: String?,
    var prix: String?,
    var imgProduit: Int
) : Parcelable
