package com.example.projet_session_omar_sebastien_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projet_session_omar_sebastien_v1.data.User
import com.example.projet_session_omar_sebastien_v1.db.AppDatabase

class CreerCompteActivity : AppCompatActivity() {

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creer_compte)

        db = AppDatabase(this)
        val entrerNom = findViewById<EditText>(R.id.entrerNom)
        val entrerEmail = findViewById<EditText>(R.id.entrerEmail)
        val entrerPassword = findViewById<EditText>(R.id.entrerPassword)
        val confirmerPassword = findViewById<EditText>(R.id.confirmerPassword)
        val enregistrer = findViewById<Button>(R.id.btnEnregistrer)
        val tvError = findViewById<TextView>(R.id.tvError)

        enregistrer.setOnClickListener{
            tvError.visibility = View.INVISIBLE
            tvError.text = ""

            val nom = entrerNom.text.toString()
            val email = entrerEmail.text.toString()
            val password = entrerPassword.text.toString()
            val confPassword = confirmerPassword.text.toString()

            //verifier si les champs sont null
            if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty())
            {
                tvError.text = getString(R.string.erreur_champs_vide)
                tvError.visibility = View.VISIBLE

            }
            else{
                //verifier si la password est egale à confirmer password
                if (password != confPassword){

                    tvError.text = getString(R.string.erreur_password_different)
                    tvError.visibility = View.VISIBLE
                }
                else{
                    val user = User(0, nom, email, password)
                    val isInserted = db.addUser(user)

                    if (isInserted){
                        entrerNom.setText("")
                        entrerEmail.setText("")
                        entrerPassword.setText("")
                        confirmerPassword.setText("")

                        Toast.makeText(this, getString(R.string.enregistrement_reussie), Toast.LENGTH_LONG).show()
                        Intent(this, HomeActivity::class.java).also {
                            it.putExtra("email", email)
                            startActivity(it)
                        }
                        finish()
                    }
                }
            }

        }



    }
}