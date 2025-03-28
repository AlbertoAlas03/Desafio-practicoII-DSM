package com.example.desafio_practicoii

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio_practicoii.estudiante.Estudiante
import com.example.desafio_practicoii.estudiante.EstudianteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.widget.*

class ListaEstudiantesActivity : AppCompatActivity() {

    private lateinit var estudiantesList: MutableList<Estudiante>

    private lateinit var recyclerView: RecyclerView
    private lateinit var estudianteAdapter: EstudianteAdapter

    private lateinit var spinnerFiltro: Spinner
    private lateinit var spinnerValores: Spinner
    private lateinit var btnFiltrar: Button

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("estudiantes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_estudiantes)


        recyclerView = findViewById(R.id.recyclerViewEstudiantes)
        recyclerView.layoutManager = LinearLayoutManager(this)


        estudiantesList = mutableListOf()

       estudianteAdapter = EstudianteAdapter(this, estudiantesList, dbRef)
        recyclerView.adapter = estudianteAdapter

        spinnerFiltro = findViewById(R.id.spinnerFiltro)
        spinnerValores = findViewById(R.id.spinnerValores)
        btnFiltrar = findViewById(R.id.btnFiltrar)


        val opcionesFiltro = listOf("Todos", "Materia", "Grado")
        val filtroAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesFiltro)
        filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFiltro.adapter = filtroAdapter

        spinnerFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                cargarOpcionesDeFiltro(opcionesFiltro[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        cargarTodosLosEstudiantes()
        btnFiltrar.setOnClickListener {
            val opcionSeleccionada = spinnerFiltro.selectedItem.toString()

            if (opcionSeleccionada == "Todos") {
                cargarTodosLosEstudiantes()
            } else if (spinnerValores.selectedItem != null) {
                val valorSeleccionado = spinnerValores.selectedItem.toString()
                filtrarEstudiantes(opcionSeleccionada, valorSeleccionado)
            } else {
                Toast.makeText(this, "Selecciona un valor para filtrar", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onBackPressed()
    }


    private fun cargarOpcionesDeFiltro(opcion: String) {
        if (opcion == "Todos") {
            spinnerValores.isEnabled = false
            spinnerValores.adapter = null
        } else {
            spinnerValores.isEnabled = true
            val valores = when (opcion) {
                "Materia" -> listOf("Matemáticas", "Ciencias", "Historia", "Inglés", "Sociales")
                "Grado" -> listOf("1°", "2°", "3°", "4°", "5°", "6°", "7°", "8°", "9°")
                else -> emptyList()
            }

            val adapterValores = ArrayAdapter(this, android.R.layout.simple_spinner_item, valores)
            adapterValores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerValores.adapter = adapterValores
        }
    }


    private fun cargarTodosLosEstudiantes() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                estudiantesList.clear()

                for (estudianteSnapshot in snapshot.children) {
                    val estudiante = estudianteSnapshot.getValue(Estudiante::class.java)
                    estudiante?.let {
                        estudiante.key = estudianteSnapshot.key
                        estudiantesList.add(it)
                    }
                }

                estudianteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun filtrarEstudiantes(campo: String, valor: String) {
        val filtro = when (campo) {
            "Materia" -> "materia"
            "Grado" -> "grado"
            else -> return
        }

        dbRef.orderByChild(filtro).equalTo(valor).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                estudiantesList.clear()

                for (estudianteSnapshot in snapshot.children) {
                    val estudiante = estudianteSnapshot.getValue(Estudiante::class.java)
                    estudiante?.let {
                        estudiantesList.add(it)
                    }
                }

                estudianteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al filtrar datos", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sing_out -> {
                Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
