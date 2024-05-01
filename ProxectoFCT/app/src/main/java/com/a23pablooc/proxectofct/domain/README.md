[//]: # (TODO: Añadir en este paquete los casos de uso)

```kotlin
package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.MedicamentoRepository
import javax.inject.Inject

class UseCase @Inject constructor(private val repository: MedicamentoRepository) {
    suspend operator fun invoke(): Any {
        // Code here
    }
}
```