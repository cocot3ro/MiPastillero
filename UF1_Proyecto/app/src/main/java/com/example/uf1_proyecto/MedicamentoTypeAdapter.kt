package com.example.uf1_proyecto

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class MedicamentoTypeAdapter : TypeAdapter<Medicamento>() {
    private val gson = Gson()
    override fun write(writer: JsonWriter, value: Medicamento) {
        writer.beginObject()

        writer.name("codNacional").value(value.codNacional)
        writer.name("fichaTecnica").value(value.fichaTecnica)
        writer.name("fichaTecnica").value(value.fichaTecnica)
        writer.name("prospecto").value(value.prospecto)
        writer.name("fechaInicio").value(value.fechaInicio)
        writer.name("fechaFin").value(value.fechaFin)
        writer.name("horario")
        gson.toJson(value.horario, MutableList::class.java, writer)
        writer.name("isFavorite").value(value.isFavorite)
        writer.endObject()
    }

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
                                            fichaTecnica = reader.nextString()
                                        }

                                        2 -> {
                                            reader.nextName()
                                            prospecto = reader.nextString()
                                        }
                                    }
                                }
                            }
                        }
                        reader.endObject()
                    }

                    reader.endArray()
                }
            }
        }

        reader.endObject()

        return Medicamento(nombre, null, fichaTecnica, prospecto, null, null, null, null)
    }

}