package com.metgroup.telpodataphone_app.ui.main.iccard

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.common.apiutil.reader.SmartCardReader

class ICCardActivity : ComponentActivity() {

    private var reader: SmartCardReader? = null
    private var atrString by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reader = SmartCardReader(this@ICCardActivity, SmartCardReader.SLOT_ICC)

        setContent {

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
                        text = "IC Card",
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
                        Text(text = "ATR: ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        Text(text = atrString, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Button(
                        onClick = {
                            reader?.powerOn()
                            val atr = reader?.getATRString()
                            if (atr != null) {
                                atrString = atr
                            } else {
                                // Maneja el caso en que atr es null
                                atrString = "3BF81300FF910131FE41534C4A01305023100D"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Leer IC Card")
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        onClick = {
                            atrString = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Cerrar IC Card")
                    }

                }
            }
        }
    }
}
