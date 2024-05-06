package com.a23pablooc.proxectofct.data.network

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CimaApiClient {
    @GET("/medicamento")
    suspend fun getMedicamentoByCodNacional(@Query("cn") cn: String): Response<MedicamentoModel>

    @GET("/medicamento")
    suspend fun getMedicamentoByNumRegistro(@Query("nregistro") nregistro: String): Response<MedicamentoModel>

}