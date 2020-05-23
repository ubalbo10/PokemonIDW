package com.example.pokemonidw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonidw.Interfaces.ApiService
import com.example.pokemonidw.clases.Pokemon
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.coroutineContext

class AdapterListaPokemon(items:List<Pokemon>): RecyclerView.Adapter<AdapterListaPokemon.ViewHolder>() {

    var ListDatos:List<Pokemon>

    init {
        this.ListDatos=items
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListaPokemon.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon
            ,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ListDatos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignarDatos(ListDatos[position])
        holder.numero.text="#${position+1}"
        var fotoUrl:String="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${position+1}.png"
        Glide.with(holder.itemView).load(fotoUrl).into(holder.foto)
        holder.itemView.setOnClickListener {
            val bundle= bundleOf("urlDetalle" to "${position+1}")
            bundle.putString("urlfoto",fotoUrl)

            Navigation.findNavController(holder.itemView).navigate(R.id.action_listarPokemonFragment_to_detallePokemonFragment,bundle)
        }




    }

    class ViewHolder(itemView: View /**/) : RecyclerView.ViewHolder(itemView){
        //buscar los widget de la vista
        var nombre=itemView.findViewById<TextView>(R.id.textView_nombre_pokemon)
        var numero=itemView.findViewById<TextView>(R.id.textView_numero_lista)
        var foto=itemView.findViewById<ImageView>(R.id.imageView_item_pokemon)






        fun asignarDatos(datos: Pokemon) {
            //asignar datos
            nombre.text="Nombre:${datos.name}"
            //ListarPokemonFragment.URLPOKEMONDETALLE=datos.url



        }







    }
}