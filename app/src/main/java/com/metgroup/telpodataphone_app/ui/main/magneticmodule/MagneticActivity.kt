    package com.metgroup.telpodataphone_app.ui.main.magneticmodule

    import android.app.Activity
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material.Button
    import androidx.compose.material.Text
    import androidx.compose.runtime.SideEffect
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.toArgb
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.common.apiutil.CommonException
    import com.common.apiutil.magnetic.MagneticCard
    import com.metgroup.telpodataphone_app.ui.printmodule.PrintModule
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.GlobalScope
    import kotlinx.coroutines.launch

    class MegneticActivity : ComponentActivity() {
        private lateinit var printModule: PrintModule
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val handler = Handler(Looper.getMainLooper())
            printModule = PrintModule(this, handler, "", "", "")

            setContent {

                val cardNumber = remember { mutableStateOf("") }
                val cardDate = remember { mutableStateOf("") }
                val buttonPressed = remember { mutableStateOf(0) }
                val firstTwoCharacters = remember { mutableStateOf("") }
                val lastTwoCharacters = remember { mutableStateOf("") }
                val requiredData = remember { mutableStateOf("") }
                val handler = Handler(Looper.getMainLooper())
                //printModule = PrintModule(this, handler)


                Box(modifier = Modifier.fillMaxSize()) {
                    val window = LocalContext.current as Activity
                    val ColorGreen = Color(0xFF3AAA35)

                    SideEffect {
                        window.window.statusBarColor = ColorGreen.toArgb()
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(ColorGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Magnetic Card",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Nombre: ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(text = requiredData.value, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Nr° de Tarjeta: ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(text = cardNumber.value, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Valida Hasta: ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(text = "${lastTwoCharacters.value}" + "/" + "${firstTwoCharacters.value}", fontWeight = FontWeight.Medium, fontSize = 17.sp)
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        Button(
                            onClick = {
                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        MagneticCard.open(applicationContext)
                                        while (buttonPressed.value == 1) {
                                            MagneticCard.startReading()
                                            val tracData = MagneticCard.check(5000)
                                            if (tracData != null) {
                                                for (i in tracData.indices) {
                                                    when (i) {

                                                        0 -> {
                                                            val data = tracData[i] ?: ""
                                                            val dataBetween = data.substringBetween('^', '^')
                                                            val dataWithoutExtraSpaces = dataBetween.replace("/", " ").replace("\\s+".toRegex(), " ")
                                                            requiredData.value = dataWithoutExtraSpaces
                                                        }
                                                        1 -> {
                                                            val data = tracData[i] ?: ""
                                                            val dataBeforeEqual = data.substringBefore('=')
                                                            val chunkedData = dataBeforeEqual.chunked(4).joinToString(" ")
                                                            cardNumber.value = chunkedData

                                                            val dataAfterEqual = data.substring(data.indexOf('=') + 1, data.indexOf('=') + 5)
                                                            firstTwoCharacters.value = dataAfterEqual.substring(0, 2)
                                                            lastTwoCharacters.value = dataAfterEqual.substring(2, 4)

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (e: CommonException) {
                                        e.printStackTrace()
                                    }
                                }
                                buttonPressed.value = 1
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Leer Tarjeta")
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {
                                cardNumber.value = ""
                                firstTwoCharacters.value = ""
                                lastTwoCharacters.value = ""
                                requiredData.value = ""
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Limpiar Datos")
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {
                                if (!printModule.isAlive) {
                                    val nombre = requiredData.value
                                    val numeroTarjeta = cardNumber.value
                                    val fechaExpiracion = " ${lastTwoCharacters.value} / ${firstTwoCharacters.value}"
                                    printModule = PrintModule(this@MegneticActivity, handler, nombre, numeroTarjeta, fechaExpiracion)
                                    printModule.start()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Impresion de Ticket")
                        }
                    }
                }
            }
        }

        fun String.substringBetween(start: Char, end: Char): String {
            val startIndex = this.indexOf(start)
            val endIndex = this.indexOf(end, startIndex + 1)
            return if (startIndex != -1 && endIndex != -1) {
                this.substring(startIndex + 1, endIndex)
            } else {
                ""
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            MagneticCard.close()
        }

    }