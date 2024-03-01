package com.metgroup.telpodataphone_app.ui.main.qrmodule

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metgroup.telpodataphone_app.R

val LocalScanResult = compositionLocalOf { mutableStateOf("") }

@Composable
fun QrScreen() {
    val scanResult = LocalScanResult.current

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
                text = "Lector QR",
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
                Text(text = "Resultado: ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text(text = scanResult.value, fontWeight = FontWeight.Medium, fontSize = 17.sp)
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    val intent = Intent().apply {
                        setClassName("com.telpo.tps550.api", "com.telpo.tps550.api.barcode.Capture")
                    }
                    try {
                        window.startActivityForResult(intent, 0x124)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(window, window.resources.getString(R.string.app_name), Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Leer QR")
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Cerrar Lector QR")
            }

        }
    }
}

class QrActivity : ComponentActivity() {
    private val scanResult = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(LocalScanResult provides scanResult) {
                QrScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x124) {
            if (resultCode == 0) {
                if (data != null) {
                    val qrcode = data.getStringExtra("qrCode") ?: ""
                    scanResult.value = qrcode
                    //Toast.makeText(this@QrActivity, "Scan result: $qrcode", Toast.LENGTH_LONG).show()
                }
            } else {
                //Toast.makeText(this@QrActivity, "Scan Failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}