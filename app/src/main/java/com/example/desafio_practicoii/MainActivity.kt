package com.example.desafio_practicoii

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar)) //show de navbar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
R.id.sing_out -> {
    AlertDialog.Builder(this).setTitle("Cerrar sesión").setMessage("¿Desea cerrar sesión?").setPositiveButton("Aceptar"){ dialog, which ->
        FirebaseAuth.getInstance().signOut().also {
            Toast.makeText(
                this,
                "Sesión cerrada",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }.setNegativeButton("Cancelar", null).show()
}
        }
        return super.onOptionsItemSelected(item)
    }
}