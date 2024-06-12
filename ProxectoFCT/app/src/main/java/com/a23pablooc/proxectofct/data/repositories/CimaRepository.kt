package com.a23pablooc.proxectofct.data.repositories

import android.net.Uri
import androidx.core.net.toUri
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.data.model.extensions.toDomain
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions.CimaImageType
import com.a23pablooc.proxectofct.data.network.CimaService
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CimaRepository @Inject constructor(
    private val cimaService: CimaService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun downloadImage(
        nregistro: String,
        imgResource: String
    ): ByteArray {
        if (nregistro.isBlank()) return byteArrayOf()
        if (imgResource.isBlank()) return byteArrayOf()

        val useImages = dataStoreManager.useImages().first()
        val useHighQualityImages = dataStoreManager.useHighQualityImages().first()

        if (!useImages) return byteArrayOf()

        var image: ByteArray? = null

        if (useHighQualityImages)
            image = cimaService.getMedImage(CimaImageType.FULL, nregistro, imgResource)

        return image
            ?: cimaService.getMedImage(CimaImageType.THUMBNAIL, nregistro, imgResource)
            ?: byteArrayOf()
    }

    suspend fun searchMedicamento(codNacional: Long): MedicamentoItem? {
        return cimaService.getMedicamentoByCodNacional(codNacional)?.apply {
            imagen = getImageResourceUri(numRegistro, imagen).toString()
        }?.toDomain()?.apply {
            pkCodNacionalMedicamento = codNacional
        }
    }

    private suspend fun getImageResourceUri(
        nregistro: String,
        imgResource: String
    ): Uri {
        if (!dataStoreManager.useImages().first()) return Uri.EMPTY

        val useHighQualityImages = dataStoreManager.useHighQualityImages().first()
        val imageType = if (useHighQualityImages) CimaImageType.FULL else CimaImageType.THUMBNAIL

        return (CimaApiDefinitions.BASE_URL + CimaApiDefinitions.FOTOS
            .replace("{imageType}", imageType.toString())
            .replace("{nregistro}", nregistro)
            .replace("{imgResource}", imgResource)).toUri()
    }
}