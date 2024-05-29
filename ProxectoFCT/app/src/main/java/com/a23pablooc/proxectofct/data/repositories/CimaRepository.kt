package com.a23pablooc.proxectofct.data.repositories

import com.a23pablooc.proxectofct.core.CimaImageType
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

//        TODO: preferencia usarImagenAltaCalidad
//        if (usarImagenAltaCalidad) {
//            return repository.downloadImage(CimaImageType.FULL, nregistro, imgResource)
//                ?: repository.downloadImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
//        }

        return cimaService.getMedImage(CimaImageType.FULL, nregistro, imgResource)
    }

    suspend fun searchMedicamento(codNacional: String): MedicamentoItem {
        var imgResource: String

        return cimaService.getMedicamentoByCodNacional(codNacional).also {
            imgResource = it.apiImagen
        }.toDomain().apply {
            runCatching {
                apiImagen = downloadImage(numRegistro, imgResource)
            }.onFailure {
                apiImagen = byteArrayOf()
            }
        }
    }
}