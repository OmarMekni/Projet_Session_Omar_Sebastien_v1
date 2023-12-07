package com.example.projet_session_omar_sebastien_v1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.projet_session_omar_sebastien_v1.db.AppDatabase

class MainActivity : AppCompatActivity() {

    //private lateinit var db: AppDatabase


    lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val connect = findViewById<Button>(R.id.connecter)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val error = findViewById<TextView>(R.id.error)
        val creerCompte = findViewById<TextView>(R.id.txtCompte)

        db = AppDatabase(this)




        //fuzione per quando si clicca sul bottone
        connect.setOnClickListener(View.OnClickListener {
            error.visibility = View.GONE
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()

            if (txtEmail.trim().isEmpty() || txtPassword.trim().isEmpty()){
                error.text = "vous devez remplir tout les champs!"
                error.visibility = View.VISIBLE
            }
            else{
//                val correctEmail = "test@gmail.com"
//                val correctPassword = "1234"
//                correctEmail == txtEmail && correctPassword == txtPassword

                val user = db.findUser(txtEmail, txtPassword)

                if (user != null){
                    email.setText("")
                    password.setText("")

                    //intent Explicite
                    Intent(this, HomeActivity::class.java).also {
                        it.putExtra("email", txtEmail)
                        startActivity(it)
                    }


                }
                else{
                    error.text = "Email ou Password n'est pas correct!"
                    error.visibility = View.VISIBLE
                }
            }



        })


        //per andare alla pagina per creare il conto
        creerCompte.setOnClickListener{
            Intent(this, CreerCompteActivity::class.java).also {
                startActivity(it)
            }
        }

    }


}