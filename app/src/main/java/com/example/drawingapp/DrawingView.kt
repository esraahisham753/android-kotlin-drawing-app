package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.createBitmap

class DrawingView(context: Context, attr: AttributeSet): View(context, attr) {
    private lateinit var drawingPath: FingerPath
    private lateinit var drawingPaint: Paint
    private var color = Color.BLACK
    private var brushThickness = 0f
    private lateinit var canvas: Canvas
    private lateinit var canvasPaint: Paint
    private lateinit var canvasBitmap: Bitmap

    init {
        setupDrawing()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w > 0 && h > 0) {
            canvasBitmap = createBitmap(w, h)
            canvas = Canvas(canvasBitmap)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (::canvasBitmap.isInitialized) {
            canvas.drawBitmap(canvasBitmap, 0f, 0f, canvasPaint)

            if (!drawingPath.isEmpty) {
                drawingPaint.apply {
                    color = drawingPath.color
                    strokeWidth = drawingPath.brushThickness
                }

                canvas.drawPath(drawingPath, drawingPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                drawingPath.apply {
                    color = color
                    brushThickness = brushThickness
                }

                drawingPath.reset()

                if (touchX != null && touchY != null) {
                    drawingPath.moveTo(touchX, touchY)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchX != null && touchY != null) {
                    drawingPath.lineTo(touchX, touchY)
                }
            }

            MotionEvent.ACTION_UP -> {
                drawingPath.apply {
                    color = color
                    brushThickness = brushThickness
                }

                drawingPaint.apply {
                    color = drawingPath.color
                    brushThickness = drawingPath.brushThickness
                }

                canvas.drawPath(drawingPath, drawingPaint)
                drawingPath.reset()

                performClick()
            }

            else -> return false
        }

        invalidate()

        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun setupDrawing() {
        brushThickness = 20f
        drawingPath = FingerPath(color, brushThickness)
        drawingPaint = Paint()

        drawingPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    fun changeBrushSize(newSize: Float) {
        brushThickness = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize,
            resources.displayMetrics
        )

        drawingPath.brushThickness = brushThickness

        drawingPaint.strokeWidth = brushThickness
    }

    internal inner class FingerPath(var color: Int, var brushThickness: Float): Path() {

    }
}