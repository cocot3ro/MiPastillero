package com.a23pablooc.proxectofct.data.network

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.a23pablooc.proxectofct.data.model.enums.CimaImageType
import javax.inject.Inject

class CimaService @Inject constructor(private val cimaApiClient: CimaApiClient) {

    suspend fun getMedicamentoByCodNacional(cn: Int): MedicamentoModel? {
        val response = cimaApiClient.getMedicamentoByCodNacional(cn)
        return response.body()
    }

    suspend fun getMedImage(
        imageType: CimaImageType,
        nregistro: String,
        imgResource: String
    ): ByteArray? {
        val response = cimaApiClient.getImagenMedicamento(imageType, nregistro, imgResource)
        return response.body()?.bytes()
    }
}