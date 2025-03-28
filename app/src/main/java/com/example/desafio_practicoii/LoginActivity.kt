package com.example.desafio_practicoii

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        //starting auth firebase
        auth = FirebaseAuth.getInstance()
//stating views
        editTextEmail = findViewById(R.id.txtEmailAddress)
        editTextPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        textViewRegister = findViewById(R.id.textViewRegister)

        btnLogin.setOnClickListener {
            login(editTextEmail.text.toString(), editTextPassword.text.toString())
        }

        textViewRegister.setOnClickListener { goToRegister() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun login(email: String, password: String){
        if(email.isEmpty() || password.isEmpty()){
Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show()
            return
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
Toast.makeText(this, "Dirección de correo electrónico no válida", Toast.LENGTH_LONG).show()
            return
        }

auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
//if all it´s ok, let´s go to the mainActivity
    if(task.isSuccessful){
        Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }else{
        Toast.makeText(this, "Error en el inicio de sesión, verifique las credenciales ingresadas", Toast.LENGTH_LONG).show()
    }
    //error message
}.addOnFailureListener{ exception ->
    //nothing to do
}
    }

    private fun goToRegister(){
val intent = Intent(this, RegisterActivity::class.java)
    startActivity(intent)
        finish()
    }
}