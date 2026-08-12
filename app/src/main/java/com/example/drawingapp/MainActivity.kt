package com.example.drawingapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var brushSizeBtn: ImageButton
    private lateinit var brushSizeDialog: Dialog
    private lateinit var brushSizeSeekBar: SeekBar
    private lateinit var brushSizeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val drawingView: DrawingView = findViewById(R.id.drawingView)


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
    }
}