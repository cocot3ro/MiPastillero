package com.cocot3ro.mipastillero.data.database.definitions

object UsuarioTableDefinition {
    const val TABLE_NAME = "tbl_usuarios"

    object Columns {
        const val PK_USUARIO = "${TABLE_NAME}__pk_usuario"
        const val NOMBRE = "${TABLE_NAME}__nombre"
    }
}