package com.example.pokemonidw

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * A simple [Fragment] subclass.
 */
class DetallePokemonFragment : Fragment() {
    lateinit var db:AppDatabase
    var url: String? = null
    var urlFoto:String?=null
    lateinit var retrofit:Retrofit
    lateinit var irafav:Button
    var mensaje=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         url=arguments?.getString("urlDetalle")
         urlFoto=arguments?.getString("urlfoto")
          db = Room.databaseBuilder(
            requireActivity().applicationContext,
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
        var compartir=view.findViewById<Button>(R.id.button_compartir)
        compartir.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                var texto="Bueno soy el pokemon numero:${numero.text} y me llamo ${nombre.text} puedes ver mi foto en ${urlFoto} " +
                        "es un gran pokemon me gusta mucho por eso lo comparto contigo echale una mirada, hasta luego"
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, texto)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        irafav=view.findViewById(R.id.button_irfavoritos)
        irafav.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_detallePokemonFragment_to_favoritosFragment)
        }
        aceptar.setOnClickListener {
            if(favorito.isChecked){
                var numero=url!!.toInt()
                var favorito:Boolean=true
                var add=Favoritos(numero,favorito)
                //insertar y update isfavorito de favoritos
                GlobalScope.launch {

                    var respuesta=db.favDao().updateFav(add)
                    Log.i("bd2",respuesta.toString())
                    if(respuesta==0){
                        var respuesta=db.favDao().insertAll(add)

                    }
                    var tamano=db.favDao().loadAllByIds(true)
                    Log.i("bd3","${tamano.size.toString()}")
                    mensaje="Este pokemon podra verlo en la lista de favoritos"

                }

            }else{
                //update a no favorito
                var numero=url!!.toInt()
                var favorito:Boolean=false
                var add=Favoritos(numero,favorito)
                GlobalScope.launch {

                    var respuesta=db.favDao().updateFav(add)
                    Log.i("bd2",respuesta.toString())
                    if(respuesta==0){
                        var respuesta=db.favDao().insertAll(add)
                    }
                    mensaje="Este pokemon no aparecera en favoritos"


                }
            }
            var handler=Handler()
            handler.postDelayed(
                {mostrarDialogo()} ,1000
            )
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

    fun createSimpleDialog(): android.app.AlertDialog? {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle("Pokemon Favoritos")
            .setMessage(mensaje)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, which ->

                })

        return builder.create()
    }
    fun mostrarDialogo(){
        createSimpleDialog()!!.show()
    }

}
