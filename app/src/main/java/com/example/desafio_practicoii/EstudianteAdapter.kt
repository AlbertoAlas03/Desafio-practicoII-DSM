package com.example.desafio_practicoii.estudiante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio_practicoii.R

class EstudianteAdapter(private val listaEstudiantes: List<Estudiante>) :
    RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {

    class EstudianteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Nombre: TextView = itemView.findViewById(R.id.txtItemNombre)
        val Materia: TextView = itemView.findViewById(R.id.txtItemMateria)
        val Grado: TextView = itemView.findViewById(R.id.txtItemGrado)
        val Nota: TextView = itemView.findViewById(R.id.txtItemNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_estudiante, parent, false)
        return EstudianteViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante = listaEstudiantes[position]
        holder.Nombre.text = "Nombre: ${estudiante.nombre} ${estudiante.apellido}"
        holder.Materia.text = "Materia: ${estudiante.materia}"
        holder.Grado.text = "Grado: ${estudiante.grado}"
        holder.Nota.text = "Nota: ${estudiante.nota}"
    }

    override fun getItemCount(): Int {
        return listaEstudiantes.size
    }
}
