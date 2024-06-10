# Codificación e Probas

## Codificación

## Prototipos
Enlace a release do [Prototipo 1](https://gitlab.iessanclemente.net/damo/a23pablooc/-/releases/v1.0.0)

| Funcionalidade                             | Realizada      |
|--------------------------------------------|----------------|
| Engadir medicamento                        | En realizacion |
| Modificar medicamento                      | En realizacion |
| Borrar medicamento                         | En realizacion |
| Engadir a favoritos                        | En realizacion |
| Quitar de favoritos                        | En realizacion |
| Alta usuario                               | En realizacion |
| Modificar usuario                          | En realizacion |
| Eliminar usuario                           | En realizacion |
| Activar/desactivar notificacions           | Non            |
| Activar/desactivar imaxes de alta calidade | Non            |
| Buscar medicamento (na api)                | Si             |
| Descargar imaxe (da api)                   | Si             |

Enlace a release do [Prototipo 2]()

| Funcionalidade                             | Realizada      |
|--------------------------------------------|----------------|
| Engadir medicamento                        | Si             |
| Modificar medicamento                      | En realizacion |
| Borrar medicamento                         | En realizacion |
| Engadir a favoritos                        | En realizacion |
| Quitar de favoritos                        | En realizacion |
| Alta usuario                               | Si             |
| Modificar usuario                          | Si             |
| Eliminar usuario                           | Si             |
| Seleccionar usuario por defecto            | Si             |
| Activar/desactivar notificacions           | Non            |
| Activar/desactivar o uso de imaxes         | Si             |
| Activar/desactivar imaxes de alta calidade | Si             |
| Buscar medicamento por codigo (na api)     | Si             |
| Descargar imaxe (da api)                   | Si             |
| Buscar medicamentos por nome (na api)      | Non            |

## Innovación

Para este proxecto utilizaronse as seguintes tecnoloxías.

- [RecyclerView](https://developer.android.com/develop/ui/views/layout/recyclerview?hl=es-419): Unha biblioteca de Android para mostrar conxuntos de datos nunha lista ou cuadricula, ofrecendo un mellor rendemento permitindo a reutilizacion de vistas e reducindo o uso de memoria. Empregase tamen [DiffUtil](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/DiffUtil) para actualizar a vista de maneira eficiente ao cambiar os datos, en vez de forzar unha recarga de toda a vista.

- [Room](https://developer.android.com/training/data-storage/room?hl=es-419): Unha biblioteca de persistencia que brinda unha capa de
abstraction sobre as bases de datos SQLite

** Problemas encontrados **
Para facer as relacións entre as entidades, xa sexan 1-1, 1-N ou N-M, debese crear unha clase a parte que relacione as duas entidades, e ao momento de realizar a consulta nos DAOs, o metodo debe devolver un objecto do tipo da clase que representa a relacion.

- [Retrofit](https://square.github.io/retrofit/): Un cliente HTTP para Java e Kotlin que permite realizar consultar a API comodamente

- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419): Unha biblioteca de inserción de dependencias

- [Flow](https://developer.android.com/kotlin/flow?hl=es-419): Unha ferramenta para manexar e emitir fluxos de datos asíncronos dun xeito secuencial e reactivo, facilitando a xestión de datos e eventos na interface de usuario.

- [Corrutinas](https://developer.android.com/kotlin/coroutines?hl=es-419): Unha caracteristica de Kotlin que permite manexar tarefas asíncronas de xeito mais sinxelo e eficiente, permitindo que o código se suspenda e reanude sin bloquear o fío de execución principal.

- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore?hl=es-419): Biblioteca deseñada para almacenar e xestionar datos de xeito eficiente e seguro, utilizando Flow's e corrutinas para a persistencia de datos clave-valor (Preferences DataStore) e a serialización de obxectos (Proto DataStore), proporcionando una alternativa mais flexible e robusta a SharedPreferences.

- [SQLCipher](https://github.com/sqlcipher/sqlcipher-android): Unha extensión de SQLite para Android proporciona cifrado da base de datos.

## Probas
