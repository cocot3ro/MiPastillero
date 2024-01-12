package com.example.uf1_proyecto.model

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL

class APITypeAdapter : TypeAdapter<Medicamento.Builder>() {

    override fun write(writer: JsonWriter, value: Medicamento.Builder) {}

    override fun read(reader: JsonReader): Medicamento.Builder {
        val builder = Medicamento.Builder()

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "nregistro" -> {
                    val numRegistro = reader.nextString()
                    builder.setNumRegistro(numRegistro)
                    builder.setUrl("https://cima.aemps.es/cima/publico/detalle.html?nregistro=${numRegistro}")
                }

                "nombre" -> {
                    builder.setNombre(reader.nextString())
                }

                "labtitular" -> {
                    builder.setLaboratorio(reader.nextString())
                }

                "cpresc" -> {
                    builder.setReceta(reader.nextString())
                }

                "docs" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "tipo" -> {
                                    when (reader.nextInt()) {
                                        1 -> {
                                            reader.nextName()
                                            reader.nextString()
                                            reader.nextName()
                                            builder.setFichaTecnica(reader.nextString())
                                        }

                                        2 -> {
                                            reader.nextName()
                                            reader.nextString()
                                            reader.nextName()
                                            builder.setProspecto(reader.nextString())
                                        }
                                    }
                                }

                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                    }

                    reader.endArray()
                }

                "fotos" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "tipo" -> {
                                    when (reader.nextString()) {
                                        "materialas" -> {
                                            reader.nextName()
                                            val url = reader.nextString()
                                            builder.setImagen(descargarImagenComoByteArray(url))
                                        }
                                    }
                                }

                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                    }
                    reader.endArray()
                }

                "principiosActivos" -> {
                    reader.beginArray()
                    val principiosActivos = mutableListOf<String>()
                    while (reader.hasNext()) {
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "nombre" -> {
                                    principiosActivos.add(reader.nextString())
                                }

                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                    }
                    reader.endArray()

                    builder.setPrincipiosActivos(principiosActivos)
                }

                "dosis" -> {
                    builder.setDosis(reader.nextString())
                }

                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return builder
    }

    private fun descargarImagenComoByteArray(url: String): ByteArray {
        return try {
            val inputStream: InputStream = URL(url).openStream()
            val buffer = ByteArray(1024)
            val output = ByteArrayOutputStream()

            var bytesRead = inputStream.read(buffer)
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }

            output.toByteArray()
        } catch (e: Exception) {
            ByteArray(0)
        }
    }
}