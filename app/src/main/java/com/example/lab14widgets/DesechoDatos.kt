package com.example.lab14widgets

import android.content.Context
import androidx.core.content.edit

object DesechoDatos {

    val capitulos = listOf(
        "Prólogo",
        "Capítulo 1",
        "Capítulo 2",
        "Capítulo 3",
        "Capítulo 4",
        "Capítulo 5",
        "Epílogo"
    )

    private const val PREFS = "el_desecho"
    private const val KEY_CAPITULO = "capitulo_actual"

    fun capituloActual(context: Context): Int {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_CAPITULO, 0)
    }

    fun guardarCapitulo(context: Context, indice: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
            putInt(KEY_CAPITULO, indice)
        }
    }

    fun nivelAmenaza(indice: Int): String {
        return when {
            indice <= 1 -> "Baja"
            indice <= 4 -> "Media"
            else -> "Alta"
        }
    }
}
