package com.amatai.lybra_app.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Contacts
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
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
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databinding.FragmentMainBinding
import com.amatai.lybra_app.ui.activities.MainActivity
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelMainFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment(), LifecycleOwner {


    val viewmodelMainFragment by viewModels<ViewmodelMainFragment> {
        VMFactory(
            RepositoryImpl(
                DataSources(AppDatabase.getDatabase(requireContext())!!)
            )
        )
    }


    companion object {
        var sessionLogueo: String? = null
        var usuarioLogueado: UsuarioLogueado? = null
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val REQUIRED_PERMISSIONS =
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private val REQUEST_CODE_PERMISSIONS = 10

    lateinit var binding: FragmentMainBinding
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
    val COUNTDOWN_TIME_VIDEO_RECORDED = 60000L
    lateinit var timerGrabancionVideo: CountDownTimer
    lateinit var videoCapture: VideoCapture
    var grabando = false
    val ONE_SECOND = 1000L
    val COUNTDOWN_TIME = 3000L
    lateinit var timer: CountDownTimer

    var contadorInicioGrabacion = 0

    private lateinit var viewFinder: TextureView

    var sesionGrabacion = 0

    //Solicitud de localizacion
    var locationRequest: LocationRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    @SuppressLint("ClickableViewAccessibility", "MissingPermission", "RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        context ?: binding.root
        inicializarLocationRequest()
        viewFinder = binding.viewFinder

        viewmodelMainFragment.obtenerContactosSinSincronizar()
        viewmodelMainFragment.borrarContactosApi()
        viewmodelMainFragment.actualizarContactoApi()


        viewmodelMainFragment.obtenerUsuarioLogueado.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                if (it != null) {
                    usuarioLogueado = it
                }
            })

        viewmodelMainFragment.obtenerDirectorio()

        viewmodelMainFragment.obtenerSessionLogueo()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                sessionLogueo = it.access_token

            })

        (activity as MainActivity).supportActionBar!!.title = ""

        if (grabando) {
            binding.grabacion.visibility = View.VISIBLE
            binding.grabandoVideo.visibility = View.VISIBLE
        } else {
            binding.grabacion.visibility = View.GONE
            binding.grabandoVideo.visibility = View.GONE
        }


        binding.botonPanico.setOnClickListener {
            if (contadorInicioGrabacion < 3) {

                var presionarParaGrabar = 0
                contadorInicioGrabacion++

                when (contadorInicioGrabacion) {
                    1 -> presionarParaGrabar = 2
                    2 -> presionarParaGrabar = 1
                }

                if (contadorInicioGrabacion != 3){
                    Toast.makeText(requireContext(), "presione ${presionarParaGrabar} veces para grabar", Toast.LENGTH_SHORT).show()
                }else{
                    if (checkIfLocationOpened()){
                        Toast.makeText(requireContext(), "Iniciando grabacion", Toast.LENGTH_SHORT).show()
                    }
                }

                if (contadorInicioGrabacion == 3) {
                    contadorInicioGrabacion = 0

                    //iniciarGrabacion(grabacion, requireContext(), binding.grabandoVideo)

                    //val intent = Intent(requireContext(), GrabarVideoActivity::class.java)
                    //requireActivity().startActivity(intent)

                    if (allPermissionsGranted()) {
                        Log.d("gpsActivo", checkIfLocationOpened().toString())
                        if (checkIfLocationOpened()){
                            enviarMensajeTexto()
                            if (sesionGrabacion == 0) {
                                viewFinder.post { startCamera() }
                            }
                        }else{
                            AlertDialog.Builder(requireContext())
                                .setMessage("Antes de grabar debes activar el gps")
                                .setPositiveButton("Aceptar"){dialog, which ->
                                    dialog.dismiss()
                                }.show()
                        }

                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                        )
                    }
                }
            }
        }

        binding.botonPararGrabacion.setOnClickListener {
            videoCapture.stopRecording()
            timerGrabancionVideo.cancel()

            grabacion.visibility = View.GONE
        }

        return binding.root
    }


    @SuppressLint("MissingPermission")
    fun enviarMensajeTexto() {
        var lat = 0.0
        var long = 0.0
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                lat = location!!.latitude
                long = location.longitude
                Log.d("ubicacion", "$lat $long")
                viewmodelMainFragment.obtenerUsuarioLogueado.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer { usuarioLogueado ->

                        viewmodelMainFragment.obtenerContactosConfianzaSqlite.asLiveData()
                            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                                //Log.d("numero", "me ejecuto + $usuarioLogueado")

                                if (!it.isNullOrEmpty()) {
                                    val sms = SmsManager.getDefault()
                                    for (i in it) {
                                        Log.d("numero", it.toString())
                                        //Log.d("numero", usuarioLogueado.toString())
                                        sms.sendTextMessage(
                                            i.number_phone,
                                            null,
                                            "${usuarioLogueado.name} puede estar en peligro, llamalo al ${usuarioLogueado.phone_number} https://www.google.com/maps/search/?api=1&query=$lat,$long",
                                            null,
                                            null
                                        )
                                    }
                                }
                            })
                    })
            }

        if (sesionGrabacion == 1) {
            iniciarGrabacion(grabacion, requireContext(), binding.grabandoVideo)
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

        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().build()
        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        val videoCaptureConfig = VideoCaptureConfig.Builder().apply {
            setTargetRotation(viewFinder.display.rotation)
        }.build()

        videoCapture = VideoCapture(videoCaptureConfig)


        preview.setOnPreviewOutputUpdateListener {

        }

        // Bind use cases to lifecycle
        CameraX.bindToLifecycle(this, preview, videoCapture)

        sesionGrabacion = 1

        val runnable = Runnable {
            iniciarGrabacion(grabacion, requireContext(), binding.grabandoVideo)
        }
        val handler = Handler()
        handler.postDelayed(runnable, 200)


    }

    @SuppressLint("RestrictedApi")
    fun iniciarGrabacion(grabacion: LinearLayout, context: Context, grabandoVideo: TextView) {
        grabacion.visibility = View.VISIBLE
        grabando = true
        var tiempo = 0

        timerGrabancionVideo = object : CountDownTimer(COUNTDOWN_TIME_VIDEO_RECORDED, ONE_SECOND) {
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
                tiempo++
                grabandoVideo.visibility = View.VISIBLE
                grabandoVideo.text = "Grabando: $tiempo"
                //Log.d("grabandooo", tiempo.toString())
            }

        }.start()

        val file = File(
            requireActivity().externalMediaDirs.first(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".mp4"
        )

        /*
        val file = File(
            "/storage/emulated/0/",
            "videoslybra"
        )

        //Log.d("rutaguardada", getOutputDirectory().toString() )

        file.mkdir()

        val video = File(
            file,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".mp4"
        )
         */


        //Toast.makeText(context, "Inicio Grabacion", Toast.LENGTH_SHORT).show()
        videoCapture.startRecording(file, object : VideoCapture.OnVideoSavedListener {

            override fun onVideoSaved(file: File?) {
                grabando = false

                val video = VideoEntity(
                    null,
                    file!!.path,
                    1
                )

                viewmodelMainFragment.agregarVideosSqlite(video)

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

    private fun checkIfLocationOpened(): Boolean {
        val provider: String = Settings.Secure.getString(
            requireActivity().contentResolver,
            Settings.Secure.LOCATION_PROVIDERS_ALLOWED
        )
        println("Provider contains=> $provider")
        return provider.contains("gps")
    }
    /*
            videoCapture.startRecording(
            video,
            ContextCompat.getMainExecutor(requireContext()),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(file: File) {
                    val video = VideoEntity(
                        null,
                        file!!.path,
                        1
                    )

                    viewmodelMainFragment.agregarVideosSqlite(video)

                    Toast.makeText(
                        context,
                        "el video se guardo aqui ${video.path}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    //Log.i(tag, "Video Error: $message")
                }

            })
     */


}


