package com.amatai.lybra_app.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amatai.lybra_app.R
import kotlinx.android.synthetic.main.activity_grabar_video.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS =
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
private val tag = MainActivity::class.java.simpleName

class GrabarVideoActivity : AppCompatActivity() {

    private lateinit var viewFinder: TextureView
    private lateinit var captureButton: Button
    private lateinit var videoCapture: VideoCapture
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grabar_video)

        viewFinder = findViewById(R.id.view_finder)
        captureButton = findViewById(R.id.capture_button)

        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }





        stop_button.setOnClickListener {
            videoCapture.stopRecording()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @SuppressLint("RestrictedApi")
    private fun startCamera() {
        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().build()
        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Create a configuration object for the video use case
        val videoCaptureConfig = VideoCaptureConfig.Builder().apply {
            setTargetRotation(viewFinder.display.rotation)
        }.build()
        videoCapture = VideoCapture(videoCaptureConfig)

        preview.setOnPreviewOutputUpdateListener {
            //viewFinder.surfaceTexture = it.surfaceTexture

        }


        val file = File(
            externalMediaDirs.first(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".mp4"
        )

        // Bind use cases to lifecycle
        CameraX.bindToLifecycle(this, preview, videoCapture)


        videoCapture.startRecording(file, object : VideoCapture.OnVideoSavedListener {
            override fun onVideoSaved(file: File?) {
                Log.i(tag, "Video File : $file")
                Toast.makeText(application, "el video se guardo aqui $file", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(
                useCaseError: VideoCapture.UseCaseError?,
                message: String?,
                cause: Throwable?
            ) {
                Log.i(tag, "Video Error: $message")
            }
        })

    }

}