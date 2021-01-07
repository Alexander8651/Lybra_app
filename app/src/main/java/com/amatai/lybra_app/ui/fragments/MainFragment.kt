package com.amatai.lybra_app.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databinding.FragmentMainBinding
import com.amatai.lybra_app.ui.activities.MainActivity
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelMainFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment(), LifecycleOwner {

    val viewmodelMainFragment by viewModels<ViewmodelMainFragment> { VMFactory(RepositoryImpl(
        DataSources(AppDatabase.getDatabase(requireContext())!!)
    )) }

    companion object{
        var sessionLogueo:String? = null
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val REQUIRED_PERMISSIONS =
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
        )

    private val REQUEST_CODE_PERMISSIONS = 10

    lateinit var mainFragmentBinding: FragmentMainBinding
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
    val COUNTDOWN_TIME_VIDEO_RECORDED = 600000L
    lateinit var timerGrabancionVideo: CountDownTimer
    var videoCapture: VideoCapture? = null

    val ONE_SECOND = 1000L
    val COUNTDOWN_TIME = 3000L
    lateinit var timer: CountDownTimer

    var sesionGrabacion = 0

    //Solicitud de localizacion
    var locationRequest: LocationRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inicializarLocationRequest()

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("ClickableViewAccessibility", "MissingPermission", "RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodelMainFragment.obtenerDirectorio()

        viewmodelMainFragment.obtenerSessionLogueo().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            sessionLogueo = it.access_token
            Log.d("sessionloggg", sessionLogueo!!)
        })

        (activity as MainActivity).supportActionBar!!.title = ""
        if (allPermissionsGranted()) {

            if (sesionGrabacion == 0){
                startCamera()
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        mainFragmentBinding = FragmentMainBinding.bind(view)

        botonPanico.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
                    override fun onFinish() {
                        enviarMensajeTexto()
                    }
                    override fun onTick(millisUntilFinished: Long) {
                        var tiempoRestante = 0
                        when {
                            millisUntilFinished > 1998.toLong() -> tiempoRestante = 3
                            millisUntilFinished == 1998.toLong() || millisUntilFinished == 1997.toLong() -> tiempoRestante =
                                2
                            millisUntilFinished < 1997.toLong() -> tiempoRestante = 1
                        }
                        mainFragmentBinding.textoEnvioMensaje.textSize = 30F
                        mainFragmentBinding.textoEnvioMensaje.text =
                            "se enviara el mensaje en $tiempoRestante"
                    }
                }.start()
            } else {
                timer.cancel()
                mainFragmentBinding.textoEnvioMensaje.textSize = 15F
                mainFragmentBinding.textoEnvioMensaje.text =
                    "Presiona el boton en caso de encontrase en peligro"
            }
            false
        }

        //boton para la grabacion
        mainFragmentBinding.botonPararGrabacion.setOnClickListener{
            timerGrabancionVideo.cancel()
            videoCapture!!.stopRecording()
            grabacion.visibility = View.GONE
        }
    }


    @SuppressLint("MissingPermission")
    fun enviarMensajeTexto() {
        var lat = 0.0
        var long = 0.0
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                lat = location!!.latitude
                long = location.longitude
                Log.d("ubicacion", "$lat $long")
                viewmodelMainFragment.obtenerUsuarioLogueado.observe(viewLifecycleOwner, androidx.lifecycle.Observer {usuarioLogueado ->

                    viewmodelMainFragment.obtenerDirectorioSqlite.asLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        Log.d("numero", "me ejecuto + $usuarioLogueado")

                        if (!it.isNullOrEmpty()){
                            val sms = SmsManager.getDefault()
                            for (i in it){
                               //Log.d("numero", i.numberPhone)
                               //Log.d("numero", usuarioLogueado.toString())
                                sms.sendTextMessage(i.number_Phone, null, "${usuarioLogueado.name} puede estar en peligro, llamalo al ${usuarioLogueado.phone_number} o su ubicacion actual es https://www.google.com/maps/search/?api=1&query=$lat,$long", null, null)
                            }
                        }
                    })
                })
            }


        if (allPermissionsGranted()) {
            iniciarGrabacion(grabacion, requireContext(), mainFragmentBinding.grabandoVideo)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun inicializarLocationRequest() {
        locationRequest = LocationRequest()
        //Solicita la Ubicacion cada 10 segundos
        locationRequest?.interval = 10000
        //Actualiza la ubicacion cada 5 segundos
        locationRequest?.fastestInterval = 5000
        // exactitud con la que se quiere obtener la ubicacion, alta para este caso
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @SuppressLint("RestrictedApi")
    private fun startCamera() {

        sesionGrabacion = 1
        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().build()
        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Create a configuration object for the video use case
        val videoCaptureConfig = VideoCaptureConfig.Builder().apply {

        }.build()
        videoCapture = VideoCapture(videoCaptureConfig)

        preview.setOnPreviewOutputUpdateListener {
            //viewFinder.surfaceTexture = it.surfaceTexture
        }
        // Bind use cases to lifecycle
        CameraX.bindToLifecycle(this, preview, videoCapture)


    }

    @SuppressLint("RestrictedApi")
    fun iniciarGrabacion(grabacion: LinearLayout, context: Context, grabandoVideo: TextView) {
        grabacion.visibility = View.VISIBLE
        var tiempo = 0

        timerGrabancionVideo = object : CountDownTimer(COUNTDOWN_TIME_VIDEO_RECORDED, ONE_SECOND){
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
                tiempo++
                grabandoVideo.text = "Grabando: $tiempo"
                //Log.d("grabandooo", tiempo.toString())
            }

        }.start()
        val file = File(
            context.externalMediaDirs.first(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".mp4"
        )

        Toast.makeText(context, "Inicio Grabacion", Toast.LENGTH_SHORT).show()
        videoCapture!!.startRecording(file, object : VideoCapture.OnVideoSavedListener {

            override fun onVideoSaved(file: File?) {
                Toast.makeText(
                    context,
                    "el video se guardo aqui $file",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(
                useCaseError: VideoCapture.UseCaseError?,
                message: String?,
                cause: Throwable?
            ) {
                //Log.i(tag, "Video Error: $message")
            }
        })
    }






}