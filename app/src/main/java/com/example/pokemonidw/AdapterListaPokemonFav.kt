package com.example.pokemonidw

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonidw.clases.Pokemon

class AdapterListaPokemonFav(items:List<Pokemon>): RecyclerView.Adapter<AdapterListaPokemonFav.ViewHolder>() {

    var ListDatos:List<Pokemon>

    init {
        this.ListDatos=items
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListaPokemonFav.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon
            ,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ListDatos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignarDatos(ListDatos[position])
        var url=ListDatos[position].url
        // ejemplo https://pokeapi.co/api/v2/pokemon/1/
        var delimitador="/"
        var urlArray=url.split(delimitador)
        var numeropokemonapi=urlArray[6]
        Log.i("valor","${urlArray[6]}")
        holder.numero.text="#${position+1}"
        var fotoUrl:String="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${numeropokemonapi}.png"
        Glide.with(holder.itemView).load(fotoUrl).into(holder.foto)
        holder.itemView.setOnClickListener {
            val bundle= bundleOf("urlDetalle" to "${numeropokemonapi}")
            bundle.putString("urlfoto",fotoUrl)

            Navigation.findNavController(holder.itemView).navigate(R.id.action_favoritosFragment_to_detallePokemonFragment,bundle)
        }




    }

    class ViewHolder(itemView: View /**/) : RecyclerView.ViewHolder(itemView){
        //buscar los widget de la vista
        var nombre=itemView.findViewById<TextView>(R.id.textView_nombre_pokemon)
        var numero=itemView.findViewById<TextView>(R.id.textView_numero_lista)
        var foto=itemView.findViewById<ImageView>(R.id.imageView_item_pokemon)






        fun asignarDatos(datos: Pokemon) {
            //asignar datos
            nombre.text="${datos.name}"
            //ListarPokemonFragment.URLPOKEMONDETALLE=datos.url



        }







    }
}