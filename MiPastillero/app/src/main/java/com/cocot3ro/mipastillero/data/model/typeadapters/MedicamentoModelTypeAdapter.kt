package com.cocot3ro.mipastillero.data.model.typeadapters

import com.cocot3ro.mipastillero.data.model.MedicamentoModel
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class MedicamentoModelTypeAdapter : TypeAdapter<MedicamentoModel>() {
    override fun write(out: JsonWriter?, value: MedicamentoModel) {}

    override fun read(`in`: JsonReader?): MedicamentoModel {
        val builder = MedicamentoModel.Builder()
        `in`?.run {

            beginObject()

            while (hasNext()) {
                when (nextName()) {
                    "nregistro" -> {
                        val numRegistro = nextString()
                        builder.numRegistro(numRegistro)
                        builder.url("https://cima.aemps.es/cima/publico/detalle.html?nregistro=${numRegistro}")
                    }

                    "nombre" -> builder.nombre(nextString())

                    "labtitular" -> builder.laboratorio(nextString())

                    "receta" -> builder.receta(nextBoolean())

                    "cpresc" -> builder.prescripcion(nextString())

                    "conduc" -> builder.afectaConduccion(nextBoolean())

                    "docs" -> {
                        beginArray()

                        while (hasNext()) {
                            beginObject()

                            while (hasNext()) {
                                when (nextName()) {
                                    "tipo" -> {
                                        when (nextInt()) {
                                            2 -> {
                                                nextName()
                                                nextString()
                                                nextName()
                                                builder.prospecto(nextString())
                                            }
                                        }
                                    }

                                    else -> skipValue()
                                }
                            }

                            endObject()
                        }

                        endArray()
                    }

                    "fotos" -> {
                        beginArray()

                        while (hasNext()) {
                            beginObject()

                            while (hasNext()) {
                                when (nextName()) {
                                    "tipo" -> {
                                        when (nextString()) {
                                            "materialas" -> {
                                                nextName()
                                                val url = nextString()
                                                builder.imagen(url.substringAfterLast("/"))
                                            }
                                        }
                                    }

                                    else -> skipValue()
                                }
                            }

                            endObject()
                        }

                        endArray()
                    }

                    "principiosActivos" -> {
                        beginArray()

                        while (hasNext()) {
                            beginObject()

                            var nombre = ""
                            var cantidad = ""
                            var unidad = ""

                            while (hasNext()) {
                                when (nextName()) {
                                    "nombre" -> nombre = nextString()
                                    "cantidad" -> cantidad = nextString()
                                    "unidad" -> unidad = nextString()

                                    else -> skipValue()
                                }
                            }

                            builder.pricipiosActivos("$nombre $cantidad $unidad")

                            endObject()
                        }
                        endArray()
                    }

                    else -> skipValue()
                }
            }

            endObject()

        }

        return builder.build()

    }

}