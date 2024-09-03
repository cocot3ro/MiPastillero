package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.CimaRepository
import javax.inject.Inject

class DownloadImageUseCase @Inject constructor(
    private val cimaRepository: CimaRepository
) {
    suspend fun invoke(nregistro: String, imgResource: String): ByteArray {
        return cimaRepository.downloadImage(nregistro, imgResource)
    }
}