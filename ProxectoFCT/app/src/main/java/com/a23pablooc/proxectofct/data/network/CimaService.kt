package com.a23pablooc.proxectofct.data.network

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import javax.inject.Inject
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions.CimaImageType

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
        return cimaApiClient.getImagenMedicamento(imageType, nregistro, imgResource).body()?.bytes()
    }
}