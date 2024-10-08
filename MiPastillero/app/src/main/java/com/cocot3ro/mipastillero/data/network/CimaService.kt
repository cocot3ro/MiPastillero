package com.cocot3ro.mipastillero.data.network

import com.cocot3ro.mipastillero.data.model.MedicamentoModel
import com.cocot3ro.mipastillero.data.network.CimaApiDefinitions.CimaImageType
import javax.inject.Inject

class CimaService @Inject constructor(private val cimaApiClient: CimaApiClient) {

    suspend fun getMedicamentoByCodNacional(cn: Long): MedicamentoModel? {
        val response = cimaApiClient.getMedicamentoByCodNacional(cn)
        return response.body()
    }

    suspend fun getMedImage(
        imageType: CimaImageType,
        nregistro: String,
        imgResource: String
    ): ByteArray? {
        return try {
            cimaApiClient.getImagenMedicamento(imageType, nregistro, imgResource).body()?.bytes()
        } catch (e: Exception) {
            null
        }
    }
}