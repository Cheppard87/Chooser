package com.uravgcode.chooser.circle

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import com.uravgcode.chooser.utils.ColorGenerator
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

open class Circle(var x: Float, var y: Float, radius: Float, var color: Int = ColorGenerator.nextColor()) {

    val center = RectF()
    val ring = RectF()

    val paint = Paint()
    val strokePaint = Paint()
    val strokePaintLight = Paint()

    var startAngle = Random.nextInt(360).toFloat()
    var sweepAngle = Random.nextInt(-360, 0).toFloat()

    var coreRadius = 0f
    private val defaultRadius = radius
    private val radiusVariance = radius * 0.08f

    var winnerCircle = false
    var hasFinger = true

    private var time = 0

    init {
        paint.color = color
        paint.style = Paint.Style.FILL_AND_STROKE

        strokePaint.color = color
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeCap = Paint.Cap.ROUND

        strokePaintLight.color = Color.argb(65, 255, 255, 255)
        strokePaintLight.style = Paint.Style.STROKE
        strokePaintLight.strokeCap = Paint.Cap.ROUND
    }

    open fun updateValues(deltaTime: Int) {
        val radius = coreRadius + radiusVariance * sin(time * 0.006).toFloat()
        val innerRadius = radius * 0.6f
        val strokeWidth = radius * 0.19f

        center.set(x - innerRadius, y - innerRadius, x + innerRadius, y + innerRadius)
        ring.set(x - radius, y - radius, x + radius, y + radius)

        paint.strokeWidth = strokeWidth
        strokePaint.strokeWidth = strokeWidth
        strokePaintLight.strokeWidth = strokeWidth

        startAngle = (startAngle + deltaTime * 0.3f) % 360
        if (sweepAngle <= 360) sweepAngle += deltaTime * 0.45f

        coreRadius = when (hasFinger) {
            true -> min(coreRadius + deltaTime * 0.6f, defaultRadius)
            false -> max(coreRadius - deltaTime * 0.6f, 0f)
        }

        time += deltaTime
    }

    open fun removeFinger() {
        if (winnerCircle) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ hasFinger = false }, 1000)
        } else {
            hasFinger = false
        }
    }
}