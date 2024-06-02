package com.a23pablooc.proxectofct.data.database.definitions

object UsuarioTableDefinition {
    const val TABLE_NAME = "tbl_usuarios"

    object Columns {
        const val PK_USUARIO = TABLE_NAME + "_" + "pk_usuario"
        const val NOMBRE = TABLE_NAME + "_" + "nombre"
    }
}