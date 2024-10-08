package com.cocot3ro.mipastillero.data.network

import com.cocot3ro.mipastillero.data.model.MedicamentoModel
import com.cocot3ro.mipastillero.data.network.CimaApiDefinitions.CimaImageType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CimaApiClient {
    @GET("rest/medicamento")
    suspend fun getMedicamentoByCodNacional(
        @Query("cn") cn: Long
    ): Response<MedicamentoModel>

    @GET("rest/medicamento")
    suspend fun getMedicamentoByNumRegistro(
        @Query("nregistro") nregistro: String
    ): Response<MedicamentoModel>

    @GET(CimaApiDefinitions.FOTOS)
    suspend fun getImagenMedicamento(
        @Path("imageType") imageType: CimaImageType,
        @Path("nregistro") nregistro: String,
        @Path("imgResource") imgResource: String
    ): Response<ResponseBody>
}