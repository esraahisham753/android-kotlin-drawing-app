package com.example.drawingapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class DrawingView(context: Context, attr: AttributeSet): View(context, attr) {
    private lateinit var drawingPath: FingerPath
    private lateinit var drawingPaint: Paint
    private var color = Color.BLACK
    private var brushThickness = 0f
    private lateinit var canvas: Canvas
    private lateinit var canvasPaint: Paint

    init {
        setupDrawing()
    }

    fun setupDrawing() {
        brushThickness = 20f
        drawingPath = FingerPath(color, brushThickness)

        drawingPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
    }

    internal inner class FingerPath(val color: Int, val brushThickness: Float): Path() {

    }
}