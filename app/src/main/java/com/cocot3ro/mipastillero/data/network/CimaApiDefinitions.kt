package com.cocot3ro.mipastillero.data.network

object CimaApiDefinitions {

    const val BASE_URL = "https://cima.aemps.es/cima/"
    const val FOTOS = "fotos/{imageType}/materialas/{nregistro}/{imgResource}"
    val codNacionalPattern = Regex("""[6-9]\d{5}(\.\d)?""")

    enum class CimaImageType(private val type: String) {
        THUMBNAIL("thumbnails"),
        FULL("full");

        override fun toString(): String {
            return type
        }
    }
}