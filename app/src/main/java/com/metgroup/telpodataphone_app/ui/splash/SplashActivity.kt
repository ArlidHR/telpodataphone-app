package com.metgroup.telpodataphone_app.ui.splash

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.metgroup.telpodataphone_app.R
import com.metgroup.telpodataphone_app.ui.splashpay.SplashPayActivity
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                navigateToSplashPay()
            }
        }
    }

    private fun navigateToSplashPay() {
        startActivity(Intent(this, SplashPayActivity::class.java))
        finish()
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.splash_met_its),
        contentDescription = "Splash Screen",
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )

    LaunchedEffect(key1 = true) {
        delay(2000)
        onTimeout()
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen {}
}