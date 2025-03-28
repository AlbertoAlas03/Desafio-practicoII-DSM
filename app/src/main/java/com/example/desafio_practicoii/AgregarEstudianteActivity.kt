package com.example.desafio_practicoii

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio_practicoii.estudiante.Estudiante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AgregarEstudianteActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var spinnerMaterias: Spinner
    private lateinit var spinnerGrados: Spinner
    private lateinit var txtNota: EditText
    private lateinit var btnGuardar: Button


    private val db = FirebaseDatabase.getInstance().getReference("estudiantes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_estudiante)
        txtNombre = findViewById(R.id.txtNombre)
        txtApellido = findViewById(R.id.txtApellido)
        spinnerMaterias = findViewById(R.id.spinnerMaterias)
        spinnerGrados = findViewById(R.id.spinnerGrados)
        txtNota = findViewById(R.id.txtNota)
        btnGuardar = findViewById(R.id.btnGuardar)
        configurarSpinners()
        btnGuardar.setOnClickListener {
            guardarEstudiante()
        }
    }

    private fun configurarSpinners() {
        val materias = listOf("Matemáticas", "Ciencias", "Historia", "Inglés", "Sociales")
        val grados = listOf("1°", "2°", "3°", "4°", "5°", "6°", "7°", "8°", "9°")

        val adapterMaterias = ArrayAdapter(this, android.R.layout.simple_spinner_item, materias)
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaterias.adapter = adapterMaterias

        val adapterGrados = ArrayAdapter(this, android.R.layout.simple_spinner_item, grados)
        adapterGrados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrados.adapter = adapterGrados
    }

    private fun guardarEstudiante() {
        val nombre = txtNombre.text.toString().trim()
        val apellido = txtApellido.text.toString().trim()
        val materia = spinnerMaterias.selectedItem.toString()
        val grado = spinnerGrados.selectedItem.toString()
        val nota = txtNota.text.toString().trim().toDoubleOrNull()
        val newKey = db.push().key
        if(newKey != null) {
            if (nombre.isEmpty() || apellido.isEmpty() || nota == null || nota > 10 || nota < 0) {
                Toast.makeText(
                    this,
                    "Todos los campos son obligatorios y tienen que estar en el rango",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val estudiante = Estudiante(nombre,apellido,materia,grado,nota.toString())
            db.child(newKey).setValue(estudiante).addOnSuccessListener{
                Toast.makeText(this,"Se guardo con exito",Toast.LENGTH_SHORT).show()
                limpiarCampos()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }.addOnFailureListener{
                Toast.makeText(this,"Error al guardar",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"No se pudo generar la key",Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        txtNombre.text.clear()
        txtApellido.text.clear()
        txtNota.text.clear()
        spinnerMaterias.setSelection(0)
        spinnerGrados.setSelection(0)
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

                    }
                }.setNegativeButton("Cancelar", null).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
