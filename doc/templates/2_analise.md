# Análise: Requirimentos do sistema

## Descrición xeral
A aplicación permitirá o rexistro, edición e borrado de tanto medicamentos como usuarios. Tamén permitirá configurar
as notificións de xeito individual para cada medicamento, ou de xeito global, desactivandoas todas.

## Funcionalidades
| Accion                             | Descrición                                                                               |
|------------------------------------|------------------------------------------------------------------------------------------|
| Engadir medicamento                | Engadir un novo medicamento na base de datos                                             |
| Modificar medicamento              | Modificar un medicamento: nome, imaxe, datas e horario                                   |
| Borrar medicamento                 | Borrar un medicamento da base de datos                                                   |
| Engadir a favoritos                | Engadir a lista de favoritos na base de datos un medicamento existente                   |
| Eliminar de favoritos              | Borrar un medicamento da lista de favoritos da base de datos                             |
| Marcar toma                        | Marcar ou desmarcar a toma dun medicamento dun día e hora                                |
| Alta usuario                       | Crear un novo perfil de usuario na base de datos                                         |
| Modificar usuario                  | Editar o nome do perfil do usuario                                                       |
| Eliminar usuario                   | Borrar o perfil de usuario e todos os seus medicamentos rexistrados da base de datos     |
| Configirar notificións             | Activar ou desactivar as notificación globais na pantalla de configuración da aplicación |
| Configurar uso de imaxes           | Activar ou desactivar a opción para que a aplicacion descarge imaxes de interet          |
| Configirar imaxes de alta calidade | Activar ou desactivar a descarga de imaxes de maior calidade, si están dispoñibles       |
| Anotacións na axenda               | Editar as entradas na axenda persoal                                                     |

## Tipos de usuarios
A aplicación contará só cun tipo de usuario, que terá acceso a todas as funcionalidades.

## Normativa
De acordo coa LOPDPGDD, a aplicación comprometese a recopilar, utilizar e almacenar os datos das persoas usuarias de xeito transparente, segura e respetuosa coa sua privacidade.
Os datos recopilados limitaranse ao nome de usuario co único proposito de indentificalo na aplicación.

### Politica de Privacidade
1. **Datos recopilados:** A aplicación recopila e almacena o nombe que a parsoa usuaria indique, co proposito exclusivo de identificala dentro da aplicación. Tamén a información dos medicamentos rexistrados para realizar o control da toma de medicamentos.

2. **Uso dos datos:** O nome de usuario e datos dos medicamento serán utilizados internamente dentro da aplicación
e non se compartirán con terceiros.

3. **Almacenamento dos datos:** Os datos dos usuarios almacenaranse localmente no dispositivo e non se tranfiren nin
almacenan en servidores externos.

### Consentimento do usuario
Quenes utilicen a aplicación aceptar a recopilación e almacenamento do seu nome, co proposito exclusivo de indentificación dentro da aplicación. Terán a opción de retirar o seu consentimento e eliminar a súa información en calquera momento.

### Seguridade dos datos
Os datos dos usuarios serán almacenados localmente no dispositivo nunha base de datos SQLite. Emplearanse métodos de cifrado 
para garantizar a confidencialidade e integridade da información.

### Dereitos dos usuarios
Quenes utilicen a aplicación terán dereito a acceder, rectificar e eliminar a súa información persoal en carquer momento. Proporcionaranse, dentro da aplicación, mecanismos para que as persoas usuarias exerzan os seus dereitos en relación cos seus datos persoais.