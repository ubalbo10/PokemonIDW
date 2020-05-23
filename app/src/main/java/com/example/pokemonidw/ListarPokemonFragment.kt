package com.example.pokemonidw

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonidw.Interfaces.ApiService
import com.example.pokemonidw.clases.ListaPokemon
import com.example.pokemonidw.clases.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class ListarPokemonFragment : Fragment() {
    lateinit var listadoPokemones:List<Pokemon>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var vista:View=inflater.inflate(R.layout.fragment_listar_pokemon, container, false)
        var recyler=vista.findViewById<RecyclerView>(R.id.recycler_pokemon)

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.URL_BASE)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val services = retrofit.create(ApiService::class.java)
        services.ObtenerListadoDePokemones()?.enqueue(object : Callback<ListaPokemon?>{
            override fun onResponse(
                call: Call<ListaPokemon?>,
                response: Response<ListaPokemon?>
            ) {
                if(response.isSuccessful){
                    var respuesta=response.body()
                    //obtengo la lista de los nombres de los 20 primeros pokemones pero sin la foto
                    listadoPokemones=respuesta!!.results

                    recyler.adapter = AdapterListaPokemon(listadoPokemones)
                    recyler!!.layoutManager =GridLayoutManager(activity,2)
                    Log.i("msj","estoy aqui")

                }
            }

            override fun onFailure(call: Call<ListaPokemon?>, t: Throwable) {
                Log.i("msj","estoy fallando")
            }

        })
        return vista
    }
companion object{
    }
}