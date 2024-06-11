package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.CimaRepository
import javax.inject.Inject

class DownloadImageUseCase @Inject constructor(
    private val cimaRepository: CimaRepository
) {
    suspend fun invoke(nregistro: String, imgResource: String): ByteArray {
        return cimaRepository.downloadImage(nregistro, imgResource)
    }
}