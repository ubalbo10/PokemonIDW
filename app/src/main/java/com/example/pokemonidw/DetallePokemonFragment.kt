package com.example.pokemonidw

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.pokemonidw.Interfaces.ApiService
import com.example.pokemonidw.clases.AppDatabase
import com.example.pokemonidw.clases.DetallePokemon
import com.example.pokemonidw.clases.Favoritos
import com.example.pokemonidw.clases.PokemonEspecie
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class DetallePokemonFragment : Fragment() {
    lateinit var db:AppDatabase
    var url: String? = null
    var urlFoto:String?=null
    lateinit var retrofit:Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         url=arguments?.getString("urlDetalle")
         urlFoto=arguments?.getString("urlfoto")
          db = Room.databaseBuilder(
            activity!!.applicationContext,
            AppDatabase::class.java, "databaseLocal"
        ).build()

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
        val view=inflater.inflate(R.layout.fragment_detalle_pokemon, container, false)
        //poner pantalla de espera
        var pantalladecarga=view.findViewById<LinearLayout>(R.id.cargando)
        var pantallaprincipal=view.findViewById<View>(R.id.pantallaprincipaldetalle)
        pantalladecarga.visibility=View.VISIBLE
        pantallaprincipal.visibility=View.INVISIBLE
        //
        Log.d("mensaje",url)
        var foto=view.findViewById<ImageView>(R.id.imageView_fotodetalle)
        var generacion=view.findViewById<Button>(R.id.button_generacion)
        var weight=view.findViewById<Button>(R.id.button_detalle_weight)
        var numero=view.findViewById<TextView>(R.id.textView_detalle_numero)
        var nombre=view.findViewById<TextView>(R.id.textView_detalle_nombre)
        var especietitulo=view.findViewById<TextView>(R.id.textView_titulo_especie)
        var especie1=view.findViewById<TextView>(R.id.textView_detalle_especie1)
        var especie2=view.findViewById<TextView>(R.id.textView_detalle_especie2)
        var recyclerhabilidades=view.findViewById<RecyclerView>(R.id.recyclerview_habilidades)
        var favorito=view.findViewById<RadioButton>(R.id.radioButton_favorito)
        var aceptar=view.findViewById<Button>(R.id.button_favorito)
        aceptar.setOnClickListener {
            if(favorito.isChecked){
                var numero=url!!.toInt()
                var favorito:Boolean=true
                var add=Favoritos(numero,favorito)
                //insertar y update isfavorito de favoritos
                GlobalScope.launch {
                    try{
                        var respuesta=db.favDao().insertAll(add)
                        Log.i("bd",respuesta.toString())

                    }catch (e: NumberFormatException) {
                        Toast.makeText(activity,"agregado anteriormente",Toast.LENGTH_LONG).show()
                    }

                    var respuesta2=db.favDao().updateFav(add)
                    Log.i("bd2",respuesta2.toString())
                    var tamano=db.favDao().loadAllByIds(true)
                    Log.i("bd3","${tamano.size.toString()}")
                }

            }
        }

        Glide.with(view).load(urlFoto).into(foto);
        especietitulo.text="Especie"
        generacion.text="Generacion"

        val services = retrofit.create(ApiService::class.java)
        services.DetallePokemon("pokemon/$url/")?.enqueue(object : Callback<DetallePokemon?>{
            override fun onFailure(call: Call<DetallePokemon?>, t: Throwable) {
                Log.d("ws","${t.message}")
            }

            override fun onResponse(
                call: Call<DetallePokemon?>,
                response: Response<DetallePokemon?>
            ) {
               if(response.isSuccessful){
                   var respuesta=response.body()
                   Log.i("respuesta",respuesta.toString())
                   nombre.text=respuesta!!.name
                   numero.text="# ${respuesta!!.id}"
                   weight.text="Weight:${respuesta.weight}"
                   var listaHabilidades=respuesta.abilities
                    //recycler horizontal
                   recyclerhabilidades.adapter = AdapterListaHabilidades(listaHabilidades)
                   recyclerhabilidades!!.layoutManager =LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                   recyclerhabilidades!!.setHasFixedSize(true)



               }
            }

        })
        services.EspeciesPokemon("pokemon-species/$url/")?.enqueue(object: Callback<PokemonEspecie?>{
            override fun onFailure(call: Call<PokemonEspecie?>, t: Throwable) {
                Log.e("ws",t.message)
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<PokemonEspecie?>,
                response: Response<PokemonEspecie?>
            ) {
                if(response.isSuccessful){
                    var respuesta=response.body()
                    var listadoespecie=respuesta!!.egg_groups
                    generacion.text=respuesta.generation.name
                    nombre.text=respuesta.name
                    numero.text="#${respuesta.id}"
                    if(listadoespecie.size==1){
                        especie2.visibility=View.INVISIBLE
                        especie1.text=listadoespecie[0].name
                    }else{
                        especie2.visibility=View.VISIBLE
                        especie1.text=listadoespecie[0].name
                        especie2.text=listadoespecie[1].name
                    }

                    //desaparecer pantalla de carga
                    pantallaprincipal.visibility=View.VISIBLE
                    pantalladecarga.visibility=View.GONE


                }

        }
        } )


        return view
    }

}
