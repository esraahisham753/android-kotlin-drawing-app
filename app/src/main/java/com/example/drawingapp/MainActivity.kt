package com.example.drawingapp

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var brushSizeBtn: ImageButton
    private lateinit var brushSizeDialog: Dialog
    private lateinit var brushSizeSeekBar: SeekBar
    private lateinit var brushSizeTextView: TextView
    private lateinit var purpleBtn: ImageButton
    private lateinit var orangeBtn: ImageButton
    private lateinit var blueBtn: ImageButton
    private lateinit var greenBtn: ImageButton
    private lateinit var redBtn: ImageButton
    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawingView = findViewById(R.id.drawingView)
        purpleBtn = findViewById(R.id.purpleBtn)
        orangeBtn = findViewById(R.id.orangeBtn)
        blueBtn = findViewById(R.id.blueBtn)
        greenBtn = findViewById(R.id.greenBtn)
        redBtn = findViewById(R.id.redBtn)


        brushSizeBtn = findViewById(R.id.brushSizeBtn)
        brushSizeBtn.setOnClickListener {
            brushSizeDialog = Dialog(this@MainActivity)
            brushSizeDialog.setContentView(R.layout.brush_dialog)

            brushSizeSeekBar = brushSizeDialog.findViewById(R.id.brushSizeSeekbar)
            brushSizeTextView = brushSizeDialog.findViewById(R.id.brushSizeTextView)

            brushSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar, p1: Int, p2: Boolean) {
                    drawingView.changeBrushSize(seekbar.progress)
                    brushSizeTextView.text = seekbar.progress.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })

            brushSizeSeekBar.progress = drawingView.getBrushSize()
            brushSizeTextView.text = drawingView.getBrushSize().toString()

            brushSizeDialog.show()
        }

        purpleBtn.setOnClickListener(this)
        orangeBtn.setOnClickListener(this)
        blueBtn.setOnClickListener(this)
        greenBtn.setOnClickListener(this)
        redBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.purpleBtn -> drawingView.changeBrushColor("#4A148C")
            R.id.orangeBtn -> drawingView.changeBrushColor("#FF8800")
            R.id.blueBtn -> drawingView.changeBrushColor("#0099CC")
            R.id.greenBtn -> drawingView.changeBrushColor("#669900")
            R.id.redBtn -> drawingView.changeBrushColor("#CC0000")
        }
    }
}