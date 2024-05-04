package com.a23pablooc.proxectofct.data.network

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import javax.inject.Inject

class CimaService @Inject constructor(private val cimaApiClient: CimaApiClient) {

    suspend fun getMedicamentoByNumRegistro(nregistro: String): MedicamentoModel.Builder {
        val response = cimaApiClient.getMedicamentoByNumRegistro(nregistro)
        return response.body() ?: throw Exception("Error al obtener medicamento con número de registro: $nregistro")
    }

    suspend fun getMedicamentoByCodNacional(cn: String) : MedicamentoModel.Builder {
        val response = cimaApiClient.getMedicamentoByCodNacional(cn)
        return response.body() ?: throw Exception("Error al obtener medicamento con código nacional: $cn")
    }
}