package com.example.lab14widgets

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider

private fun colorFijo(color: Color) = ColorProvider(day = color, night = color)

class DesechoWidgetContent : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        val KEY_CAPITULO = intPreferencesKey("capitulo_actual")

        suspend fun actualizarTodos(context: Context, indice: Int) {
            val ids = GlanceAppWidgetManager(context).getGlanceIds(DesechoWidgetContent::class.java)
            ids.forEach { id ->
                updateAppWidgetState(context, id) { prefs ->
                    prefs[KEY_CAPITULO] = indice
                }
                DesechoWidgetContent().update(context, id)
            }
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ContenidoDesecho()
        }
    }

    @Composable
    private fun ContenidoDesecho() {
        val context = LocalContext.current
        val estado = currentState<Preferences>()
        val indice = estado[KEY_CAPITULO] ?: DesechoDatos.capituloActual(context)
        val total = DesechoDatos.capitulos.size

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .background(Color(0xFF0D0F0D))
                .cornerRadius(20.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EL DESECHO",
                    modifier = GlanceModifier.defaultWeight(),
                    style = TextStyle(
                        color = colorFijo(Color(0xFFC62828)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "● Amenaza: ${DesechoDatos.nivelAmenaza(indice)}",
                    style = TextStyle(color = colorFijo(Color(0xFF8BC34A)), fontSize = 12.sp)
                )
            }
            Spacer(GlanceModifier.height(6.dp))
            Text(
                text = DesechoDatos.capitulos[indice],
                style = TextStyle(
                    color = colorFijo(Color.White),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Progreso: ${indice + 1} / $total",
                style = TextStyle(color = colorFijo(Color(0xFFB0B0B0)), fontSize = 12.sp)
            )
            Spacer(GlanceModifier.height(10.dp))
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                Button(
                    text = "Siguiente",
                    onClick = actionRunCallback<AvanzarCapituloAction>(),
                    modifier = GlanceModifier.defaultWeight(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorFijo(Color(0xFFC62828)),
                        contentColor = colorFijo(Color.White)
                    )
                )
                Spacer(GlanceModifier.width(8.dp))
                Button(
                    text = "Capítulos",
                    onClick = actionStartActivity<CapitulosActivity>(),
                    modifier = GlanceModifier.defaultWeight(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorFijo(Color(0xFF33691E)),
                        contentColor = colorFijo(Color.White)
                    )
                )
            }
        }
    }
}

class AvanzarCapituloAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val actual = DesechoDatos.capituloActual(context)
        val siguiente = if (actual < DesechoDatos.capitulos.size - 1) actual + 1 else 0
        DesechoDatos.guardarCapitulo(context, siguiente)
        DesechoWidgetContent.actualizarTodos(context, siguiente)
    }
}
