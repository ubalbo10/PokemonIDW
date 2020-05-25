package com.example.pokemonidw

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.pokemonidw.Interfaces.ApiService
import com.example.pokemonidw.clases.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class ListarPokemonFragment : Fragment() {
    lateinit var listadoPokemones:List<Pokemon>
    lateinit var retrofit:Retrofit
    lateinit var db:AppDatabasePokemones
    lateinit var recyler:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofit = Retrofit.Builder()
            .baseUrl(ApiService.URL_BASE)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabasePokemones::class.java, "dbLocalPokemones"
        ).build()
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var vista:View=inflater.inflate(R.layout.fragment_listar_pokemon, container, false)
         recyler=vista.findViewById<RecyclerView>(R.id.recycler_pokemon)
        var loading=vista.findViewById<LinearLayout>(R.id.pantallaloading)
        loading.visibility=View.VISIBLE

        //comprobamos si tenemos conexion a internet
        if(comprobaciondeinter()){
            Log.i("tengointer","si")
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
                        var contador=0
                        for(pokemon in listadoPokemones ){
                            //llenamos bdlocal
                            contador=contador+1
                            var habilidad1="ir"
                            var habilidad2="reir"
                            var nombre=pokemon.name
                            //necesita seis paremetros y solo tengo dos aqui los demas son de la siguiente pantalla

                            var pokemonRoom:PokemonRoom=PokemonRoom(contador,nombre,"v","v",
                            1,habilidad1,habilidad2)

                            GlobalScope.launch {

                                var respuesta=db.PokeDao().updatePok(pokemonRoom)
                                Log.i("bd2",respuesta.toString())
                                if(respuesta==0){
                                    var respuesta=db.PokeDao().insertAll(pokemonRoom)

                                }

                            }
                        }
                        loading.visibility=View.GONE
                        recyler.adapter = AdapterListaPokemon(listadoPokemones)
                        recyler!!.layoutManager =GridLayoutManager(activity,2)
                        recyler!!.setHasFixedSize(true)
                        Log.i("msj","estoy aqui")

                    }
                }

                override fun onFailure(call: Call<ListaPokemon?>, t: Throwable) {
                    Log.i("msj","estoy fallando")
                    loading.visibility=View.GONE
                    mostrarDialogo("Fallo del webservices","intentelo mas tarde hay problema" +
                            "en la api")

                }

            })

        }else{
            //flujo sin internet tenemos que poblar de datos almacenados en la base de datos con room

            Log.i("tengointer","no")
            mostrarDialogo("No posee inter","Se mostrara datos locales, porfavor verifique su conexion a internet")
            GlobalScope.launch {
                var contador=0
                var respuesta=db.PokeDao().getAll()
                var listadopokemonesarray=ArrayList<Pokemon>()//porque el adaptador del recycle pide list luego lo casteare
                for(pokemon in respuesta){
                    contador=0+1
                    var nombre=pokemon.nombre
                    var url="https://pokeapi.co/api/v2/pokemon/$contador/"
                    var pokemonadd=Pokemon(nombre!!,url)
                    listadopokemonesarray.add(pokemonadd)
                }
                listadoPokemones=listadopokemonesarray.toList()
                loading.visibility=View.GONE
                llenadoRecyclerView()
            }



        }
        return vista
    }
    fun comprobaciondeinter():Boolean{

        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        var isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        Log.i("estado",isConnected.toString())
        return isConnected
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
    fun llenadoRecyclerView(){
        recyler.adapter = AdapterListaPokemon(listadoPokemones)
        recyler!!.layoutManager =GridLayoutManager(activity,2)
        recyler!!.setHasFixedSize(true)
    }
}
