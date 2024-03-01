package com.metgroup.telpodataphone_app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.metgroup.telpodataphone_app.ui.theme.TelpodataphoneappTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.metgroup.telpodataphone_app.R
import com.metgroup.telpodataphone_app.ui.main.iccard.ICCardActivity
import com.metgroup.telpodataphone_app.ui.main.magneticmodule.MegneticActivity
import com.metgroup.telpodataphone_app.ui.main.nfcmodule.NfcActivity
import com.metgroup.telpodataphone_app.ui.main.qrmodule.QrActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TelpodataphoneappTheme {
                val dosisBold = FontFamily(Font(R.font.dosis_bold))
                val cardTitles = listOf("Magnetic Card", "IC Card", "Lector QR", "NFC")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Header()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .offset(y = (-45).dp)
                    ) {
                        items(cardTitles) { title ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val rippleIndication = rememberRipple(color = Color(0xFF2A3E2C))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight(0.25f)
                                    .padding(horizontal = 20.dp, vertical = 7.dp)
                                    .clickable(interactionSource = interactionSource, indication = rippleIndication) {
                                        if (title == "Magnetic Card") {
                                            val intent = Intent(this@MainActivity, MegneticActivity::class.java)
                                            startActivity(intent)
                                        }
                                        if (title == "NFC") {
                                            val intent = Intent(this@MainActivity, NfcActivity::class.java)
                                            startActivity(intent)
                                        }
                                        if (title == "IC Card") {
                                            val intent = Intent(this@MainActivity, ICCardActivity::class.java)
                                            startActivity(intent)
                                        }
                                        if (title == "Lector QR") {
                                            val intent = Intent(this@MainActivity, QrActivity::class.java)
                                            startActivity(intent)
                                        }
                                    },
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = title,
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = dosisBold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    val colorGreen = Color(0xFF3AAA35)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(colorGreen)
    ) {
    }
}