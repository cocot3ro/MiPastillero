package com.example.uf1_proyecto

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class APITypeAdapter : TypeAdapter<Medicamento>() {

    override fun write(writer: JsonWriter, value: Medicamento) {}

    override fun read(reader: JsonReader): Medicamento {
        lateinit var nombre: String
        lateinit var fichaTecnica: String
        lateinit var prospecto: String

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "nombre" -> {
                    nombre = reader.nextString()
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
                                            fichaTecnica = reader.nextString()
                                        }

                                        2 -> {
                                            reader.nextName()
                                            reader.nextString()
                                            reader.nextName()
                                            prospecto = reader.nextString()
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

                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return MedicamentoBuilder()
            .setNombre(nombre)
            .setFichaTecnica(fichaTecnica)
            .setProspecto(prospecto)
            .build()
    }

}