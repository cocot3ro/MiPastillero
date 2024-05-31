package com.a23pablooc.proxectofct.data.model.enums

enum class CimaImageType(private val type: String) {
    THUMBNAIL("thumbnail"),
    FULL("full");

    override fun toString(): String {
        return type
    }
}