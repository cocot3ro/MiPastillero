package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.core.CimaImageType
import com.a23pablooc.proxectofct.data.PillboxRepository
import javax.inject.Inject

class DownloadImageUseCase @Inject constructor(private val repository: PillboxRepository) {

    //TODO: Implementar preferencias para descargar imagen en alta calidad
    suspend operator fun invoke(nregistro: String, imgResource: String): ByteArray? {
//        if (usarImagenAltaCalidad) {
//            return repository.downloadImage(CimaImageType.FULL, nregistro, imgResource)
//                ?: repository.downloadImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
//        }

        return repository.downloadImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
    }
}