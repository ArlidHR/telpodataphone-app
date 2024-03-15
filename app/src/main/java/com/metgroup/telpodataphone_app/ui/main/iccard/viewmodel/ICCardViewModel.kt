package com.metgroup.telpodataphone_app.ui.main.iccard.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.common.apiutil.reader.SmartCardReader

class ICCardViewModel : ViewModel() {

    private var reader: SmartCardReader? = null
    val atrString = MutableLiveData<String?>("")

    fun initializeReader(activity: Activity) {
        reader = SmartCardReader(activity, SmartCardReader.SLOT_ICC)
    }

    fun readICCard() {
        reader?.powerOn()
        val atr = reader?.getATRString()
        if (atr != null) {
            atrString.value = atr
        } else {
            atrString.value = "3BF81300FF910131FE41534C4A01305023100D"
        }
    }

    fun closeICCard() {
        atrString.value = ""
    }
}