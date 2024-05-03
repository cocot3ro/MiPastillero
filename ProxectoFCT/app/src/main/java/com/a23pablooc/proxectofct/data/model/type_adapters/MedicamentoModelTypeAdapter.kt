package com.a23pablooc.proxectofct.data.model.type_adapters

import com.a23pablooc.proxectofct.data.model.MedicamentoModel
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.net.URL

class MedicamentoModelTypeAdapter : TypeAdapter<MedicamentoModel.Builder>() {
    override fun write(out: JsonWriter?, value: MedicamentoModel.Builder) {}

    override fun read(`in`: JsonReader?): MedicamentoModel.Builder {
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

                    "cpresc" -> builder.prescripcion(nextString())

                    "conduc" -> builder.conduccion(nextBoolean())

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

                                            else -> skipValue()
                                        }
                                    }

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
                                                builder.imagen(downloadImage(nextString()))
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

                    else -> skipValue()
                }
            }
            endObject()
        }

        return builder
    }

    private fun downloadImage(url: String): ByteArray {
        return URL(url).readBytes()
    }
}
