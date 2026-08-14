package com.example.drawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

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
    private lateinit var undoBtn: ImageButton
    private lateinit var colorPickerBtn: ImageButton
    private lateinit var galleryBtn: ImageButton

    private val openGalleryLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
            uri ->
        uri?.let {
            findViewById<ImageView>(R.id.galleryImage).setImageURI(it)
        }
    }

    private val permissionRequest: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions() ) {
            permissions ->
        var isGranted = false
        permissions.entries.forEach {
            if (it.value) isGranted = true
        }

        if (isGranted) openGalleryLauncher.launch("image/*")
        else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
    }

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
        undoBtn = findViewById(R.id.undoBtn)
        colorPickerBtn = findViewById(R.id.colorPickerBtn)
        galleryBtn = findViewById(R.id.galleryBtn)


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
        undoBtn.setOnClickListener(this)
        colorPickerBtn.setOnClickListener(this)
        galleryBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.purpleBtn -> drawingView.changeBrushColor("#4A148C")
            R.id.orangeBtn -> drawingView.changeBrushColor("#FF8800")
            R.id.blueBtn -> drawingView.changeBrushColor("#0099CC")
            R.id.greenBtn -> drawingView.changeBrushColor("#669900")
            R.id.redBtn -> drawingView.changeBrushColor("#CC0000")
            R.id.undoBtn -> drawingView.undo()
            R.id.colorPickerBtn -> showColorPickerDialog()
            R.id.galleryBtn -> {
                val isGranted =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        getRequiredPermissions().any { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
                    }
                    else {
                        getRequiredPermissions().all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
                    }

                if (isGranted) {
                    openGalleryLauncher.launch("image/*")
                } else {
                    showRequest()
                }
            }
        }
    }

    private fun showColorPickerDialog() {
        ColorPickerDialog.Builder(this)
            .setTitle("Color Picker")
            .setPositiveButton("Confirm", object : ColorEnvelopeListener {
                override fun onColorSelected(
                    envelope: ColorEnvelope?,
                    fromUser: Boolean
                ) {
                    drawingView.changeBrushColor("#${envelope?.hexCode}")
                }
            })
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()}
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .show()
    }

    private fun showRequest() {
        val permissions = getRequiredPermissions()

        val shouldShowRationale = permissions.any {
            shouldShowRequestPermissionRationale(it)
        }
        if (shouldShowRationale) {
            showRationaleDialog("Access to Gallery Required", "Please, you need to grant our app access to external storage to be able to edit images")
        } else {
            permissionRequest.launch(permissions)
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        val permissions = getRequiredPermissions()

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") {
                _, _ ->
                    permissionRequest.launch(permissions)

            }
            .setNegativeButton("Cancel") {
                dialog, _ ->
                    dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getRequiredPermissions(): Array<String> {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                arrayOf(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, Manifest.permission.READ_MEDIA_IMAGES)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        return permissions
    }
}