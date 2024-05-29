package com.a23pablooc.proxectofct.data.network

import com.a23pablooc.proxectofct.core.CimaImageType
import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import javax.inject.Inject

class CimaService @Inject constructor(private val cimaApiClient: CimaApiClient) {

    suspend fun getMedicamentoByCodNacional(cn: String) : MedicamentoModel {
        val response = cimaApiClient.getMedicamentoByCodNacional(cn)
        return response.body() ?: throw Exception("Error al obtener medicamento con código nacional: $cn")
    }

    suspend fun getMedImage(imageType: CimaImageType, nregistro: String, imgResource: String): ByteArray {
        val response = cimaApiClient.getImagenMedicamento(imageType, nregistro, imgResource)
        return response.body()?.bytes() ?: throw Exception("Error al obtener imagen del medicamento con número de registro: $nregistro")
    }
}