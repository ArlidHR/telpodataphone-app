package com.metgroup.telpodataphone_app.ui.printmodule

import android.os.Handler
import com.common.apiutil.printer.UsbThermalPrinter
import com.metgroup.telpodataphone_app.R
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PrintModule(
    private val context: Context,
    private val handler: Handler,
    private val nombre: String,
    private val numeroTarjeta: String,
    private val fechaExpiracion: String
) : Thread() {

    private val NOPAPER = 3
    private val CANCELPROMPT = 10
    private val PRINTERR = 11
    private val OVERHEAT = 12
    private var printGray: Int = 1
    private var nopaper: Boolean = false
    private lateinit var Result: String
    private lateinit var mUsbThermalPrinter: UsbThermalPrinter
    private var consecutiveId = 1

    init {
        mUsbThermalPrinter = UsbThermalPrinter(context)

    }
    private fun getBitmap(context: Context, resId: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    override fun run() {
        super.run()
        try {

            val currentDateTime = LocalDateTime.now()
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
            val currentTime = currentDateTime.format(timeFormatter)
            val currentDate = currentDateTime.format(dateFormatter)

            mUsbThermalPrinter.reset()
            mUsbThermalPrinter.setGray(printGray)
            mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE)
            val bitmap = getBitmap(context, R.drawable.met_pay_header)
            val resizedBitmap = resizeBitmap(bitmap, 400, 400)
            mUsbThermalPrinter.printLogo(resizedBitmap, false)
            mUsbThermalPrinter.addString("MET GROUP SAS")
            mUsbThermalPrinter.addString("HORA: $currentTime")
            mUsbThermalPrinter.addString("FECHA: $currentDate")
            mUsbThermalPrinter.addString("ID: 1")
            mUsbThermalPrinter.addString("***************************")
            mUsbThermalPrinter.addString("Nombre: $nombre")
            mUsbThermalPrinter.addString("Nro de Tarjeta: $numeroTarjeta")
            mUsbThermalPrinter.addString("Fecha de Expiración: $fechaExpiracion")
            mUsbThermalPrinter.printString()
            consecutiveId++
            mUsbThermalPrinter.walkPaper(20)
        } catch (e: Exception) {
            e.printStackTrace()
            Result = e.toString()
            if (Result.contains("NoPaperException")) {
                nopaper = true
            } else if (Result.contains("OverHeatException")) {
                handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0, null))
            } else {
                handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0, null))
            }
        } finally {
            handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0, null))
            if (nopaper) {
                handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0, null))
                nopaper = false
                return
            }
        }
    }

    fun resizeBitmap(source: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = source.width
        val height = source.height
        val scaleWidth = maxWidth.toFloat() / width
        val scaleHeight = maxHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(source, 0, 0, width, height, matrix, true)
    }
}