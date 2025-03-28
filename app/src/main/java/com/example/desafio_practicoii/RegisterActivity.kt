package com.example.desafio_practicoii

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var buttonRegister: Button
    private lateinit var textViewLogin: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.txtEmail)
        editTextPassword = findViewById(R.id.txtPass)
        buttonRegister = findViewById(R.id.btnRegister)
        textViewLogin = findViewById(R.id.textViewLogin)

        //verify if the user is auth
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        buttonRegister.setOnClickListener {
            register(editTextEmail.text.toString(), editTextPassword.text.toString())
        }

        textViewLogin.setOnClickListener { goToLogin() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
//function for register
        private fun register(email:String, password: String){
            //validations
            if(email.isEmpty() || password.isEmpty()){
Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show()
                return
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
Toast.makeText(this, "Dirección de correo electrónico no válida", Toast.LENGTH_LONG).show()
                return
            }else if(password.length < 6){
Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
                return
            }
           auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
               if(task.isSuccessful){ //if all it´s ok, let´s to MainActivity
                   Toast.makeText(this, "Cuenta creada correctamente!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
           }.addOnFailureListener{ exception ->
               Toast.makeText(
                   applicationContext,
                   exception.localizedMessage,
                   Toast.LENGTH_LONG
               ).show()}
        }
//if the user is already account
    private fun goToLogin(){
val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
    }

    override fun onResume(){
        super.onResume()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener(authStateListener)
    }
}