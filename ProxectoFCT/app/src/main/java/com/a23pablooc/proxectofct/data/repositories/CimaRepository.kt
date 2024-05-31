package com.a23pablooc.proxectofct.data.repositories

import android.util.Log
import com.a23pablooc.proxectofct.data.model.enums.CimaImageType
import com.a23pablooc.proxectofct.data.model.extensions.toDomain
import com.a23pablooc.proxectofct.data.network.CimaService
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import javax.inject.Inject

class CimaRepository @Inject constructor(
    private val cimaService: CimaService
) {
    private suspend fun downloadImage(
        nregistro: String,
        imgResource: String
    ): ByteArray {
//        TODO: preferencias imagenes

        if (imgResource.isBlank()) return byteArrayOf()
        if (imgResource.isBlank()) return byteArrayOf()

//        if (!usarImagenes) return byteArrayOf()
//
//        return if (usarImagenesAltaCalidad) {
//          cimaService.getMedImage(CimaImageType.FULL, nregistro, imgResource)
//              ?: cimaService.getMedImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
//              ?: byteArrayOf()
//        } else {
//            cimaService.getMedImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
//                ?: byteArrayOf()
//        }

        return cimaService.getMedImage(CimaImageType.FULL, nregistro, imgResource)
            ?: cimaService.getMedImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
            ?: byteArrayOf()
    }

    suspend fun searchMedicamento(codNacional: Int): MedicamentoItem? {
        var imgResource = ""

        return cimaService.getMedicamentoByCodNacional(codNacional)?.also {
            imgResource = it.imagen
        }?.toDomain()?.apply {
            imagen = try {
                downloadImage(numRegistro, imgResource)
            } catch (e: Exception) {
                byteArrayOf()
            }
        }
    }
}