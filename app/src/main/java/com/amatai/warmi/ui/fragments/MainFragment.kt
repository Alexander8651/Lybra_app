package com.amatai.warmi.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.media.MediaRecorder
import android.os.*
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.amatai.warmi.R
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.databasemanager.entities.*
import com.amatai.warmi.databinding.FragmentMainBinding
import com.amatai.warmi.ui.activities.MainActivity
import com.amatai.warmi.ui.service.PlayerService
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelMainFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.lang.Runnable
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
        var geocoder: Geocoder? = null
        lateinit var contextFragment: Context
        lateinit var contactosEnviarMensaje: List<ContactosEntity>
        lateinit var ubicacion: LocationManager
        var configuracion: Configuracion? = null
        lateinit var contextxt: Context
        var ultimaLocalizacion: Location? = null
    }

    lateinit var recorder: MediaRecorder
    lateinit var archivo: File
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
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE
        )

    private val REQUEST_CODE_PERMISSIONS = 10
    lateinit var binding: FragmentMainBinding
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
    val COUNTDOWN_TIME_VIDEO_RECORDED = 600000L
    lateinit var timerGrabancionVideo: CountDownTimer
    lateinit var videoCapture: VideoCapture
    var grabando = false
    val ONE_SECOND = 1000L

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

        if (!allPermissionsGranted()) {
            findNavController().navigate(R.id.action_mainFragment_to_otro2Fragment)
        }

        contextxt = requireActivity()

        setHasOptionsMenu(true)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        ubicacion = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        contextFragment = requireContext()

        recorder = MediaRecorder()

        context ?: binding.root
        inicializarLocationRequest()
        viewFinder = binding.viewFinder
        //contactosEnviarMensaje = listOf<ContactosEntity>()

        viewmodelMainFragment.obtenerContactosSinSincronizar()
        viewmodelMainFragment.borrarContactosApi()
        viewmodelMainFragment.actualizarContactoApi()
        viewmodelMainFragment.sincronizarReportes()

        viewmodelMainFragment.obtenerContactosConfianzaSqlite.asLiveData()
            .observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    contactosEnviarMensaje = it
                    Log.d("meejecuto", it.toString())
                })

        miLocalizacionLitener.toastDesplegado.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                Log.d("toastttt", it.toString())
                if (it == 1) {
                    Toast.makeText(requireContext(), "Se envio mensaje", Toast.LENGTH_SHORT).show()
                    miLocalizacionLitener.toastDesplegado.value = 0
                }
            })

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
                Log.d("tokennn", sessionLogueo!!)
                viewmodelMainFragment.listarReporte(sessionLogueo!!)
            })

        (activity as MainActivity).supportActionBar!!.title = ""

        if (grabando) {
            binding.grabacion.visibility = View.VISIBLE
            binding.grabandoVideo.visibility = View.VISIBLE
        } else {
            binding.grabacion.visibility = View.GONE
            binding.grabandoVideo.visibility = View.GONE
        }

        bontonPanico()

        binding.botonPararGrabacion.setOnClickListener {
            videoCapture.stopRecording()
            timerGrabancionVideo.cancel()
            grabacion.visibility = View.GONE

            if (miLocalizacionLitener.remover != null) {
                ubicacion.removeUpdates(miLocalizacionLitener.remover!!)
            }
        }

        viewmodelMainFragment.obtenerConfiguracion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            val intent = Intent(requireContext(), PlayerService::class.java)

            if (it.activarBotonFisico!!) {
                requireActivity().startService(intent)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        ultimaLocalizacion = location!!
                        Log.d("acaacacca", ultimaLocalizacion?.latitude.toString())

                    }
            }else{
                requireContext().stopService(intent)
            }
        })

        return binding.root
    }

    private fun bontonPanico() {
        viewmodelMainFragment.obtenerConfiguracion.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {

                configuracion = it
                Log.d("configaracionn", configuracion.toString())
                if (configuracion != null) {
                    if (configuracion!!.botonPanico!!) {
                        if (configuracion?.botonPanico!!) {
                            binding.botonPanico.setOnClickListener {
                                if (contadorInicioGrabacion < 2) {

                                    var presionarParaGrabar = 0
                                    contadorInicioGrabacion++

                                    when (contadorInicioGrabacion) {
                                        1 -> presionarParaGrabar = 1
                                    }

                                    if (contadorInicioGrabacion != 2) {
                                        if (configuracion?.grabarVideoAudio!!) {
                                            Toast.makeText(
                                                requireContext(),
                                                "presione ${presionarParaGrabar} veces para grabar",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            if (configuracion?.enviarMensaje!!) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "presione ${presionarParaGrabar} vez para enviar mensaje",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        if (configuracion?.grabarVideoAudio!!) {
                                            if (checkIfLocationOpened()) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Iniciando grabacion",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }
                                    }

                                    if (contadorInicioGrabacion == 2) {
                                        contadorInicioGrabacion = 0

                                        if (configuracion?.grabarVideoAudio!!) {
                                            if (allPermissionsGranted()) {
                                                //Log.d("gpsActivo", checkIfLocationOpened().toString())
                                                if (checkIfLocationOpened()) {
                                                    enviarMensajeTexto()
                                                    if (sesionGrabacion == 0) {
                                                        viewFinder.post { startCamera() }
                                                    }
                                                } else {
                                                    AlertDialog.Builder(requireContext())
                                                        .setMessage("Antes de grabar debes activar el gps")
                                                        .setPositiveButton("Aceptar") { dialog, which ->
                                                            dialog.dismiss()
                                                        }.show()
                                                }

                                            } else {
                                                ActivityCompat.requestPermissions(
                                                    requireActivity(),
                                                    REQUIRED_PERMISSIONS,
                                                    REQUEST_CODE_PERMISSIONS
                                                )
                                            }
                                        } else {
                                            if (configuracion?.enviarMensaje!!) {
                                                enviarMensajeTexto()
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                } else {
                    binding.botonPanico.setOnClickListener {
                        Toast.makeText(
                            requireContext(),
                            "No diste permiso del botón paníco",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }


    @SuppressLint("MissingPermission")
    fun enviarMensajeTexto() {

        if (ubicacion.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d("localizacionn", "Red")

            ubicacion.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0.0F,
                miLocalizacionLitener(viewmodelMainFragment)
            )


        } else {

            ubicacion.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                600000,
                0.0F,
                miLocalizacionLitener(viewmodelMainFragment)
            )
        }

        viewmodelMainFragment.obtenerConfiguracion.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {

                if (it != null) {
                    if (it.grabarVideoAudio!!) {
                        if (sesionGrabacion == 1) {
                            iniciarGrabacion(grabacion, requireContext(), binding.grabandoVideo)
                        }
                    }
                }

            })
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
        binding.botonPanico.isEnabled = false
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


        //Toast.makeText(context, "Inicio Grabacion", Toast.LENGTH_SHORT).show()
        videoCapture.startRecording(file, object : VideoCapture.OnVideoSavedListener {

            @SuppressLint("MissingPermission")
            override fun onVideoSaved(file: File?) {
                grabando = false


                requireActivity().runOnUiThread {
                    binding.botonPanico.isEnabled = true
                }


                val video = VideoEntity(
                    null,
                    file!!.path,
                    1
                )

                viewmodelMainFragment.agregarVideosSqlite(video)

                Toast.makeText(
                    context,
                    "Se creo el video",
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

    private class miLocalizacionLitener(val viewmodelMainFragment: ViewmodelMainFragment) :
        LocationListener {

        var envioMensaje = 0


        companion object {
            var remover: MainFragment.miLocalizacionLitener? = null
            val toastDesplegado = MutableLiveData<Int>()
        }

        private val FILENAME_FORMATt = "yyyy-MM-dd HH-mm"

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        @InternalCoroutinesApi
        override fun onLocationChanged(location: Location) {
            //Log.d("localizacionn", location.toString())
            var lat = location.latitude
            var long = location.longitude
            Log.d("ubicacion", "$lat $long")
            Log.d("me ejecuto", contactosEnviarMensaje.size.toString())
            remover = this

            val listener = this

            val dataSources = DataSources(AppDatabase.getDatabase(MainActivity.context!!)!!)
            val repository = RepositoryImpl(dataSources)

            var job = Job()
            var uiScope = CoroutineScope(job + Dispatchers.IO)

            uiScope.launch {
                Log.d("me ejecuto", contactosEnviarMensaje.size.toString())

                if (!contactosEnviarMensaje.isNullOrEmpty()) {
                    Log.d("me ejecuto", contactosEnviarMensaje.size.toString())
                    val sms = SmsManager.getDefault()
                    // Log.d("numero", contactosEnviarMensaje.toString())

                    for (i in contactosEnviarMensaje) {
                        Log.d("ENciando", i.number_phone)
                        //Log.d("numero", usuarioLogueado.toString())
                        val mensaje =
                            "${usuarioLogueado!!.name} puede estar en peligro, llama al ${usuarioLogueado!!.phone_number} https://www.google.com/maps/search/?api=1&query=$lat,$long"

                        if (envioMensaje == 0) {
                            if (configuracion?.enviarMensaje!!) {
                                sms.sendTextMessage(
                                    i.number_phone,
                                    null,
                                    mensaje,
                                    null,
                                    null
                                )
                                toastDesplegado.postValue(1)
                                ubicacion.removeUpdates(listener)
                            }
                        }

                        try {
                            var direccion: List<Address> = geocoder!!.getFromLocation(lat, long, 1)

                            Log.d("localizacionn", direccion.toString())

                            val ciudad =
                                "Se envió desde ${direccion[0].locality + "," + direccion[0].adminArea + "," + direccion[0].countryName}"
                            val direccionEnviado = direccion[0].getAddressLine(0)
                            Log.d("reportecreado", ciudad.toString())
                            Log.d("reportecreado", direccionEnviado.toString())

                            val creado =
                                SimpleDateFormat(FILENAME_FORMATt, Locale.US).format(
                                    System.currentTimeMillis()
                                )

                            //Log.d("createat", creado)
                            val mensajeApi =
                                "https://www.google.com/maps/search/?api=1&query=$lat,$long"
                            if (envioMensaje == 0) {
                                val reporte = ReportesEntity(
                                    1,
                                    0,
                                    mensajeApi,
                                    long.toString(),
                                    lat.toString(),
                                    usuarioLogueado!!.id,
                                    creado,
                                    creado,
                                    creado,
                                    direccionEnviado,
                                    ciudad
                                )
                                Log.d("reportecreado", reporte.toString())
                                viewmodelMainFragment.crearRepote(reporte)
                                ubicacion.removeUpdates(listener)
                            }
                        } catch (e: Exception) {
                           // viewmodelMainFragment.crearRepote(reporte)
                               Log.e("esto","esto")
                            Toast.makeText(contextxt, "No guardo reporteeeeeeeeeeeeeeeeeeee", Toast.LENGTH_SHORT).show()
                        }
                    }
                    envioMensaje = 1
                }
            }
        }
    }

    fun grabarAudio() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        val path = File(
            requireActivity().externalMediaDirs.first(),
            "audio"
        )

        path.mkdir()

        try {
            archivo = File.createTempFile(
                SimpleDateFormat(
                    "yyyyMMddHHmm",
                    Locale.US
                ).format(System.currentTimeMillis()), ".3gp", path
            )
        } catch (e: IOException) {
        }

        recorder.setOutputFile(archivo.absolutePath)
        try {
            recorder.prepare()
        } catch (e: IOException) {
        }
        recorder.start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menuaudio, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menuaudio -> {
                // Log.d("configurrrr", configuracion.toString())
                if (configuracion != null) {
                    if (configuracion!!.grabarVideoAudio!!) {
                        var contador = 0
                        grabarAudio()

                        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                        val li = LayoutInflater.from(requireContext())
                        val promptsView: View = li.inflate(R.layout.layoutalertdialog, null)
                        alertDialog.setView(promptsView)
                        val tiempograbacion =
                            promptsView.findViewById<TextView>(R.id.tiempoGrabacion)
                        val timer: CountDownTimer
                        timer = object : CountDownTimer(20000, 1000) {
                            override fun onFinish() {

                            }

                            override fun onTick(millisUntilFinished: Long) {
                                contador++
                                tiempograbacion.text = contador.toString()
                            }

                        }.start()
                        alertDialog
                            .setCancelable(false)
                            .setPositiveButton("Parar") { dialog, which ->
                                //Toast.makeText(requireContext(), archivo.absolutePath, Toast.LENGTH_SHORT).show()
                                recorder.stop()
                                val audioEntity = AudioEntity(
                                    null,
                                    archivo.toString(),
                                    1
                                )
                                viewmodelMainFragment.guardarAudio(audioEntity)
                            }.show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No diste permiso del botón paníco",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


