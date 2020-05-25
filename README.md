# PokemonIDW

El siguiente repositorio contiene la app para una prueba tecnica de una empresa de desarrollo digital , en ella se hace uso de la api de pokemon https://pokeapi.co/
para consumir datos.
PANTALLAS
1- pantalla donde se listan los primeros 20 pokemones de la api de pokemon
esta pantalla muestra el nombre del pokemon, el numero en la lista y una foto que tambien es recuperada de una url que esta en la api.
2-Pantalla de detalle de pokemon en esta se detallan los datos del pokemon como: especies, habilidades, nombre, tamano entre otro.
3- Pantalla donde podemos ver los pokemones que seleccionamos como favoritos.
La pantalla de detalle tambien cuenta con las funciones:
1-marcar o desmarcar los pokemones favoritos.
2-Compartir un texto plano por correo,whasap , facebook o por donde se desee.
3-boton que muestra la pantalla de pokemones favoritos.
CARACTERISTICAS GENERALES
-la app funciona con internet y tambien sin internet debido a que cuenta con base de datos local en caso de no reconocer conectividad de internet.
-dialogos que le muestra si:
1-tiene problemas la app al recuperar los datos de la api de pokemon
2-muestra si tiene internet
-animacion en el cambio de fragmentos donde se debe de esperar que recupere los datos (animaciones de loading).
-se puede compartir informacion de pokemon de tipo texto por cualquier medio que el dispositivo reconozca adecuado por ejemplo whasapp, facebook , correo , etc
-UI tanto para pantalla vertical como horizontal.
-cuenta con un icono personalizado
-cuenta con un menu lateral donde se puede ingresar solo a dos pantallas
1-Home
2-Favoritos
donde : HOME es la pantalla donde se listan los 20 primeros pokemones
Favoritos donde se muestra la lista de pokemones selecionado como favoritos.
INSTALACION
instalacion por medio de un archivo apk que encontrara aqui en la carpeta src
COMO USARLA.
--la pantalla principal cuenta con la lista donde podemos presionar con nuestro dedo sobre un pokemon y nos redireccionara a la siguiente pantalla de detalle.
--pantalla de detalle en ella podemos visualizar datos del pokemon y seleccionar pokemon como favorito o como no favorito.
para marcar un pokemon como favorito debemos de marcar el radiogrup que dice Favorito y presionar en el boton aceptar.
para poner el pokemon como NO favorito debemos de dejar el radiogrup en blanco y presionar en aceptar.
en la pantalla de detalle podemos compartir informacion del pokemon que tenemos en pantalla presionando en compartir y luego seleccionando el medio por el cual compartir,
siempre en la pantalla de detalle tenemos la opcion de navegar a la pantalla de favoritos presionando en el boton verfavoritos.
--pantalla de favoritos. esta pantalla tiene la misma funcionalidad que la pantalla principal.
La app cuenta con un menu donde podemos navegar hacia la pantalla principal y hacia la pantalla que muestra los pokemones favoritos.
LIBRERIAS UTILIZADAS.
algunas de las librerias utlizadas son :
1-Glide
2-ROOM
3-LOTTIE   https://github.com/airbnb/lottie-android
4-NAVIGATION
5-RETROFIT
6-GSON
algunas de estas librerias son de Android Jetpack
entre esas cosas de Android Jetpack que use estan navhostfragment,Room, cardView,RecyclerView,etc
