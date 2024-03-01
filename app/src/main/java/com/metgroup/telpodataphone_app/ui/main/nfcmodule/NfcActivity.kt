package com.metgroup.telpodataphone_app.ui.main.nfcmodule

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
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
import com.common.apiutil.nfc.Nfc
import com.common.apiutil.util.StringUtil

class NfcActivity : ComponentActivity() {

    private val CHECK_NFC_TIMEOUT = 1
    private val SHOW_NFC_DATA = 2
    private var isChecking = false
    private lateinit var handler: Handler
    val nfc = Nfc(this)
    private val A_CPU: Byte = 1
    private val A_M1: Byte = 2

    // Define las variables aquí
    private val cardType = mutableStateOf("")
    private val atqaData = mutableStateOf("")
    private val sakData = mutableStateOf("")
    private val uidData = mutableStateOf("")

    init {
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    CHECK_NFC_TIMEOUT -> {
                        Toast.makeText(this@NfcActivity, "", Toast.LENGTH_LONG).show()
                        // Aquí puedes actualizar tus botones y otros elementos de la interfaz de usuario

                    }
                    SHOW_NFC_DATA -> {
                        val uid_data = msg.obj as ByteArray

                        when (uid_data[0]) {
                            0x42.toByte() -> {
                                // Aquí puedes manejar los datos de la tarjeta de tipo B

                            }
                            0x41.toByte() -> {
                                // Aquí puedes manejar los datos de la tarjeta de tipo A
                                val atqa = ByteArray(2)
                                val sak = ByteArray(1)
                                val uid = ByteArray(uid_data[5].toInt())
                                var type: String? = null

                                uid_data.copyInto(atqa, 0, 2, 4) //[1]~[2]
                                uid_data.copyInto(sak, 0, 4, 5) //[3]
                                uid_data.copyInto(uid, 0, 6, 6 + uid_data[5])

                                when (uid_data[1]) {
                                    A_CPU -> {
                                        type = "CPU"
                                    }
                                    A_M1 -> {
                                        type = "M1"
                                        //authenticateBtn.isEnabled = true
                                    }
                                    else -> {
                                        type = "unknow"
                                    }
                                }

                                cardType.value = type
                                atqaData.value = StringUtil.toHexString(atqa)
                                sakData.value = StringUtil.toHexString(sak)
                                uidData.value = StringUtil.toHexString(uid)

                            }
                            0x46.toByte() -> {
                                // Aquí puedes manejar los datos de la tarjeta de tipo F

                            }
                            else -> {
                                Log.e(TAG, "unknow type card!!")
                            }
                        }
                    }
                }
            }
        }
    }
    private inner class ReadThread : Thread() {
        override fun run() {
            isChecking = true
            while (isChecking) {
                try {
                    nfc.open()
                    val nfcData = nfc.activate(10 * 1000) // 10s
                    if (nfcData != null) {
                        handler.sendMessage(handler.obtainMessage(SHOW_NFC_DATA, nfcData))
                        isChecking = false
                    } else {
                        Log.d(TAG, "Check MagneticCard timeout...")
                        handler.sendMessage(handler.obtainMessage(CHECK_NFC_TIMEOUT, null))
                    }
                } catch (e: CommonException) {
                    e.printStackTrace()
                }
            }
            isChecking = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        text = "NFC",
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
                        Text(text = "Card Type: ", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        Text(text = cardType.value, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ATQA: ",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(text = atqaData.value, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SAK: ",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(text = sakData.value, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "BCD: ",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(text = uidData.value.hexToBCD().joinToString(""), fontWeight = FontWeight.Medium, fontSize = 17.sp)
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Button(
                        onClick = {
                            if (!isChecking) {
                                val readThread = ReadThread()
                                readThread.start()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Leer NFC")
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        onClick = {
                            nfc.close()
                            cardType.value = ""
                            atqaData.value = ""
                            sakData.value = ""
                            uidData.value = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Cerrar NFC")
                    }

                }
            }
        }
    }
}

fun String.hexToBCD(): ByteArray {
    val data = if (this.length % 2 != 0) "0$this" else this
    return data.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}
