package com.metgroup.telpodataphone_app.ui.main.iccard.view

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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
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
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.metgroup.telpodataphone_app.ui.main.iccard.viewmodel.ICCardViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.metgroup.telpodataphone_app.ui.theme.TelpodataphoneappTheme

class ICCardActivity : ComponentActivity() {

    private var reader: SmartCardReader? = null
    private val viewModel: ICCardViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reader = SmartCardReader(this@ICCardActivity, SmartCardReader.SLOT_ICC)

        setContent {

            val atrString by viewModel.atrString.observeAsState("")
            TelpodataphoneappTheme {
                MainInterface(activity = this, viewModel)
            }
        }
    }
}

@Composable
fun MainInterface(activity: ICCardActivity, viewModel: ICCardViewModel) {

    val atrString by viewModel.atrString.observeAsState("")

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
                atrString?.let { Text(text = it, fontWeight = FontWeight.Medium, fontSize = 17.sp) }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    viewModel.readICCard()
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
                    viewModel.closeICCard()
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
