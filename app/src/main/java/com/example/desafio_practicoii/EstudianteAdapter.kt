package com.example.desafio_practicoii.estudiante

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio_practicoii.EditarEstudianteActivity
import com.example.desafio_practicoii.R
import com.google.firebase.database.DatabaseReference

class EstudianteAdapter(private val context: Context,
                        private val estudiantes: MutableList<Estudiante>,
                        private val dbRef: DatabaseReference
) : RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_item_estudiante, parent, false)
        return EstudianteViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante = estudiantes[position]
        holder.nombre.text = "${estudiante.nombre} ${estudiante.apellido}"
        holder.materia.text = estudiante.materia
        holder.grado.text = estudiante.grado
        holder.nota.text = "Nota: ${estudiante.nota}"
        holder.btnEditar.setOnClickListener {
            val estudiante = estudiantes[position]
            mostrarDialogoEditar(estudiante)
        }
        holder.btnEliminar.setOnClickListener {
            val estudianteId = estudiante.key
            mostrarDialogoEliminar(estudianteId.toString())
        }
    }

    override fun getItemCount() = estudiantes.size

    inner class EstudianteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtItemNombre)
        val materia: TextView = view.findViewById(R.id.txtItemMateria)
        val grado: TextView = view.findViewById(R.id.txtItemGrado)
        val nota: TextView = view.findViewById(R.id.txtItemNota)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
    }

    private fun mostrarDialogoEditar(estudiante: Estudiante) {
        val intent = Intent(context, EditarEstudianteActivity::class.java)
        intent.putExtra("ESTUDIANTE_ID", estudiante.key)
        intent.putExtra("NOMBRE", estudiante.nombre)
        intent.putExtra("APELLIDO", estudiante.apellido)
        intent.putExtra("MATERIA", estudiante.materia)
        intent.putExtra("GRADO", estudiante.grado)
        intent.putExtra("NOTA", estudiante.nota)
        context.startActivity(intent)
    }

    private fun mostrarDialogoEliminar(estudianteId: String) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Eliminar Estudiante")
            .setMessage("¿Estás seguro de que quieres eliminar este estudiante?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                eliminarEstudiante(estudianteId)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }


    private fun eliminarEstudiante(estudianteId: String) {
        val estudianteRef = dbRef.child(estudianteId)
        estudianteRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
                estudiantes.removeIf { it.key == estudianteId }
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Error al eliminar estudiante", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
