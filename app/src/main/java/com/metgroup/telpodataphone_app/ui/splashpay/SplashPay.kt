package com.metgroup.telpodataphone_app.ui.splashpay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metgroup.telpodataphone_app.ui.main.MainActivity
import com.metgroup.telpodataphone_app.R
import com.metgroup.telpodataphone_app.ui.main.magneticmodule.MegneticActivity
import kotlinx.coroutines.delay

class SplashPayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecondSplashScreen {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        //startActivity(Intent(this, MegneticActivity::class.java))
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

@Composable
fun SecondSplashScreen(onTimeout: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.splash_met_pay),
        contentDescription = "Second Splash Screen",
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )

    LaunchedEffect(key1 = true) {
        delay(2000)
        onTimeout()
    }
}