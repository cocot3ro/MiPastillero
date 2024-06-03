package com.a23pablooc.proxectofct.data.network

object CimaApiDefinitions {

    const val BASE_URL = "https://cima.aemps.es/cima/"

    const val FOTOS = "fotos/{imageType}/materialas/{nregistro}/{imgResource}"

    enum class CimaImageType(private val type: String) {
        THUMBNAIL("thumbnails"),
        FULL("full");

        override fun toString(): String {
            return type
        }
    }

}