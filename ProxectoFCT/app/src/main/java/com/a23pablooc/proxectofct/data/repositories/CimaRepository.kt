package com.a23pablooc.proxectofct.data.repositories

import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.data.model.enums.CimaImageType
import com.a23pablooc.proxectofct.data.model.extensions.toDomain
import com.a23pablooc.proxectofct.data.network.CimaService
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CimaRepository @Inject constructor(
    private val cimaService: CimaService,
    private val dataStoreManager: DataStoreManager
) {
    private suspend fun downloadImage(
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
        var imgResource = ""

        return cimaService.getMedicamentoByCodNacional(codNacional)?.also {
            imgResource = it.imagen
        }?.toDomain()?.apply {
            pkCodNacionalMedicamento = codNacional
            imagen = try {
                downloadImage(numRegistro, imgResource)
            } catch (e: Exception) {
                byteArrayOf()
            }
        }
    }
}