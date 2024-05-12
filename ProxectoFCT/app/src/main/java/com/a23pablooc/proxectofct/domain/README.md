[//]: # (TODO: Añadir en este paquete los casos de uso)

```kotlin
package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.PillboxRepository
import javax.inject.Inject

class UseCase @Inject constructor(private val repository: PillboxRepository) {
    suspend operator fun invoke(): Any {
        // Code here
    }
}
```