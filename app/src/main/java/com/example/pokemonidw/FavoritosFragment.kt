package com.example.pokemonidw

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.pokemonidw.Interfaces.ApiService
import com.example.pokemonidw.clases.AppDatabase
import com.example.pokemonidw.clases.Favoritos
import com.example.pokemonidw.clases.ListaPokemon
import com.example.pokemonidw.clases.Pokemon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class FavoritosFragment : Fragment() {
    lateinit var db:AppDatabase
    lateinit var retrofit:Retrofit
    lateinit var irahome:Button
    lateinit var recyclerfav: RecyclerView
    lateinit var listfavoritos:List<Favoritos>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "databaseLocal"
        ).build()
        GlobalScope.launch{
            listfavoritos=db.favDao().loadAllByIds(true)


        }

        retrofit = Retrofit.Builder()
            .baseUrl(ApiService.URL_BASE)
            .addConverterFactory(MoshiConverterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_favoritos, container, false)
        irahome=view.findViewById(R.id.button_regresar_home)
        recyclerfav=view.findViewById(R.id.recycler_favoritos)
        var pantallaLoading=view.findViewById<LinearLayout>(R.id.pantallaloading)
        var pantallaprincipal=view.findViewById<View>(R.id.pantallaprincipalfavorito)
        pantallaprincipal.visibility=View.INVISIBLE
        pantallaLoading.visibility=View.VISIBLE
        irahome.setOnClickListener {

            Navigation.findNavController(view).navigate(R.id.action_favoritosFragment_to_listarPokemonFragment)
        }

        val services = retrofit.create(ApiService::class.java)
        services.ObtenerListadoDePokemones()?.enqueue(object : Callback<ListaPokemon?> {
            override fun onResponse(
                call: Call<ListaPokemon?>,
                response: Response<ListaPokemon?>
            ) {
                if(response.isSuccessful){
                    var respuesta=response.body()
                    //loading.visibility=View.GONE
                    //obtengo la lista de los nombres de los 20 primeros pokemones pero sin la foto
                    var listadoPokemones=respuesta!!.results
                    var pokemonesFavoritos=ArrayList<Pokemon>()

                    for(pokemon in listfavoritos){
                        var id=pokemon.numero
                        pokemonesFavoritos.add(listadoPokemones[id-1])


                    }
                    var listarPokemonesFavoritos=pokemonesFavoritos.toList()
                    recyclerfav.adapter = AdapterListaPokemonFav(listarPokemonesFavoritos)
                    recyclerfav!!.layoutManager = GridLayoutManager(activity,2)
                    recyclerfav!!.setHasFixedSize(true)
                    pantallaprincipal.visibility=View.VISIBLE
                    pantallaLoading.visibility=View.GONE
                    Log.i("msj","estoy aqui")

                }
            }

            override fun onFailure(call: Call<ListaPokemon?>, t: Throwable) {
                Log.i("msj","estoy fallando")
                //poner un dialogo
            }

        })
        return view
    }

}
