package com.example.pokemonidw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonidw.clases.Abilities


class AdapterListaHabilidades(items:List<Abilities>): RecyclerView.Adapter<AdapterListaHabilidades.ViewHolder>() {

    var ListDatos:List<Abilities>

    init {
        this.ListDatos=items
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListaHabilidades.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_habilidades_pokemon
            ,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ListDatos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignarDatos(ListDatos[position])

    }

    class ViewHolder(itemView: View /**/) : RecyclerView.ViewHolder(itemView){
        //buscar los widget de la vista
        var nombre=itemView.findViewById<Button>(R.id.button_habilidades)

        fun asignarDatos(datos: Abilities) {
            //asignar datos
            nombre.text="${datos.ability.name}"

        }

    }
}