package com.example.pokemonidw

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import com.example.pokemonidw.clases.*
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
    lateinit var dbpokemones:AppDatabasePokemones
    lateinit var retrofit:Retrofit
    lateinit var irahome:Button
    lateinit var recyclerfav: RecyclerView
    lateinit var listfavoritos:List<Favoritos>
    lateinit var listPokemonLocales:List<PokemonRoom>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "databaseLocal"
        ).build()

        dbpokemones = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabasePokemones::class.java, "dbLocalPokemones"
        ).build()
        GlobalScope.launch{
            listfavoritos=db.favDao().loadAllByIds(true)
            listPokemonLocales=dbpokemones.PokeDao().getAll()


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
        //irahome=view.findViewById(R.id.button_regresar_home)
        recyclerfav=view.findViewById(R.id.recycler_favoritos)
        var pantallaLoading=view.findViewById<LinearLayout>(R.id.pantallaloading)
        var pantallaprincipal=view.findViewById<View>(R.id.pantallaprincipalfavorito)
        pantallaprincipal.visibility=View.INVISIBLE
        pantallaLoading.visibility=View.VISIBLE
//        irahome.setOnClickListener {
//
//            Navigation.findNavController(view).navigate(R.id.action_favoritosFragment_to_listarPokemonFragment)
//        }

        val services = retrofit.create(ApiService::class.java)
        if(comprobaciondeinter()){

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
                    mostrarDialogo("Problema en la api","No se pudo recuperar los datos de la api de pokemon")
                }

            })
        }else{
            //mostrar dialogo
            mostrarDialogo("Sin Internet","No posee Internet verifique su conexion " +
                    "por favor, se mostraran datos locales de tener almacenados")
            var listadoPokemones=listPokemonLocales

            var pokemonesFavoritos=ArrayList<Pokemon>()
            var contador=0
            for(pokemon in listfavoritos){

                var id=pokemon.numero   //tiene el numero del pokemon
                var url="https://pokeapi.co/api/v2/pokemon/$id/"
                //estos datos necesita la lista
                var name=listadoPokemones[id-1].nombre
                var add=Pokemon(name!!,url)
                //
                pokemonesFavoritos.add(add)


            }
            var listarPokemonesFavoritos=pokemonesFavoritos.toList()
            recyclerfav.adapter = AdapterListaPokemonFav(listarPokemonesFavoritos)
            recyclerfav!!.layoutManager = GridLayoutManager(activity,2)
            recyclerfav!!.setHasFixedSize(true)
            pantallaprincipal.visibility=View.VISIBLE
            pantallaLoading.visibility=View.GONE



        }
        return view
    }
    fun createSimpleDialog(titulo:String,mensaje:String): android.app.AlertDialog? {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, which ->

                })

        return builder.create()
    }
    fun mostrarDialogo(titulo:String,mensaje:String){
        createSimpleDialog(titulo,mensaje)!!.show()
    }

    fun comprobaciondeinter():Boolean{

        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        var isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        Log.i("estado",isConnected.toString())
        return isConnected
    }

}
