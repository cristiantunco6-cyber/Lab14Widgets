package com.example.lab14widgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.launch

val FondoDesecho = Color(0xFF0D0F0D)
val TarjetaDesecho = Color(0xFF1B1F1B)
val RojoDesecho = Color(0xFFC62828)
val VerdeBaba = Color(0xFF8BC34A)

class CapitulosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaCapitulos()
        }
    }
}

@Composable
fun PantallaCapitulos() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var actual by remember { mutableIntStateOf(DesechoDatos.capituloActual(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoDesecho)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Text("EL DESECHO", color = RojoDesecho, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text("Capítulos", color = Color.White, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            "Amenaza: ${DesechoDatos.nivelAmenaza(actual)}",
            color = VerdeBaba,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(DesechoDatos.capitulos) { indice, nombre ->
                val seleccionado = indice == actual
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            actual = indice
                            DesechoDatos.guardarCapitulo(context, indice)
                            scope.launch { DesechoWidgetContent().updateAll(context) }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TarjetaDesecho),
                    border = BorderStroke(2.dp, if (seleccionado) RojoDesecho else Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(nombre, color = Color.White, fontSize = 18.sp)
                        if (seleccionado) {
                            Text("Estás aquí", color = RojoDesecho, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
