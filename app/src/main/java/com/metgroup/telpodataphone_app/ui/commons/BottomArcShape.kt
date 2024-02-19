package com.metgroup.telpodataphone_app.ui.commons

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Size

class BottomArcShape(private val arcHeight: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - arcHeight)
            addArc(
                Rect(
                    left = 0f,
                    top = size.height - 2 * arcHeight,
                    right = size.width,
                    bottom = size.height + arcHeight
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 180f
            )
            lineTo(0f, size.height)
            close()
        })
    }
}