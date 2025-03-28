package com.example.desafio_practicoii

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio_practicoii.estudiante.Estudiante
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditarEstudianteActivity : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var spinnerMaterias: Spinner
    private lateinit var spinnerGrados: Spinner
    private lateinit var txtNota: EditText
    private lateinit var btnGuardar: Button

    private lateinit var estudianteId: String

    private val db = FirebaseDatabase.getInstance().getReference("estudiantes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_estudiante)

        txtNombre = findViewById(R.id.txtEditarNombre)
        txtApellido = findViewById(R.id.txtEditarApellido)
        spinnerMaterias = findViewById(R.id.spinnerEditarMaterias)
        spinnerGrados = findViewById(R.id.spinnerEditarGrados)
        txtNota = findViewById(R.id.txtEditarNota)
        btnGuardar = findViewById(R.id.btnEditarGuardar)

        configurarSpinners()
        estudianteId = intent.getStringExtra("ESTUDIANTE_ID") ?: ""
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val apellido = intent.getStringExtra("APELLIDO") ?: ""
        val materia = intent.getStringExtra("MATERIA") ?: ""
        val grado = intent.getStringExtra("GRADO") ?: ""
        val nota = intent.getStringExtra("NOTA") ?: ""

        txtNombre.setText(nombre)
        txtApellido.setText(apellido)
        val materiaPosition = (spinnerMaterias.adapter as ArrayAdapter<String>).getPosition(materia)
        spinnerMaterias.setSelection(materiaPosition)
        val gradoPosition = (spinnerGrados.adapter as ArrayAdapter<String>).getPosition(grado)
        spinnerGrados.setSelection(gradoPosition)
        txtNota.setText(nota)

        btnGuardar.setOnClickListener {
            actualizarEstudiante()
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

    private fun actualizarEstudiante() {
        val nombre = txtNombre.text.toString().trim()
        val apellido = txtApellido.text.toString().trim()
        val materia = spinnerMaterias.selectedItem.toString()
        val grado = spinnerGrados.selectedItem.toString()
        val nota = txtNota.text.toString().trim().toDoubleOrNull()

        if (nombre.isEmpty() || apellido.isEmpty() || nota == null || nota > 10 || nota < 0) {
            Toast.makeText(this, "Todos los campos son obligatorios y tienen que estar en el rango", Toast.LENGTH_SHORT).show()
            return
        }


        val estudiante = Estudiante(nombre, apellido, grado, materia, nota.toString())


        db.child(estudianteId).setValue(estudiante).addOnSuccessListener {
            Toast.makeText(this, "Estudiante actualizado con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al actualizar estudiante", Toast.LENGTH_SHORT).show()
        }
    }
}
