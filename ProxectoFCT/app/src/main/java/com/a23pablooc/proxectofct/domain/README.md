[//]: # (TODO: Añadir en este paquete los casos de uso)

Los casos de uso se inyectan en los ViewModel

```kotlin
package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import javax.inject.Inject

class UseCase @Inject constructor(private val repository: PillboxRepository) {
    suspend operator fun invoke(): Any {
        // Code here
    }
}
```