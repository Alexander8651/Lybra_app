package com.amatai.warmi.ui.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Address
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.SmsManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.media.VolumeProviderCompat
import com.amatai.warmi.R
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.databasemanager.entities.AudioEntity
import com.amatai.warmi.databasemanager.entities.ReportesEntity
import com.amatai.warmi.ui.activities.MainActivity
import com.amatai.warmi.ui.fragments.MainFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PlayerService : Service() {
    private var mediaSession: MediaSessionCompat? = null
    lateinit var context: Context
    var ubicacion: LocationManager? = null
    var broadcastReceiver: BroadcastReceiver? = null

    var recorder: MediaRecorder = MediaRecorder()
    lateinit var archivo: File
    var conteoEnvioMensaje = 2
    var conteoRabacion = 0



    override fun onCreate() {
        super.onCreate()
        context = this

        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession!!.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        mediaSession!!.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    0,
                    0f
                ) //you simulate a player which plays something.
                .build()
        )

        var grabando:Boolean? = null


        //this will only work on Lollipop and up, see https://code.google.com/p/android/issues/detail?id=224134
        val myVolumeProvider: VolumeProviderCompat = object : VolumeProviderCompat(
            VOLUME_CONTROL_RELATIVE,  /*max volume*/
            100,  /*initial volume level*/
            50
        ) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onAdjustVolume(direction: Int) {
                /*
                -1 -- volume down
                1 -- volume up
                0 -- volume button released
                 */


                if (direction == -1) {
                    conteoEnvioMensaje--
                    Log.d("sifuniono", conteoEnvioMensaje.toString())

                   if (grabando==null){

                       if (conteoEnvioMensaje == 0) {
                           val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.iphonenotificacion)
                           mediaPlayer.start()
                           grabando = true

                           conteoEnvioMensaje = 2

                           val runnable = Runnable {
                               mediaPlayer.stop()
                               grabarAudio()

                           }
                           val handler = Handler()
                           handler.postDelayed(runnable, 1000)

                           conteoRabacion = 0


                       }
                   }
                }

                if (direction == +1) {

                    var envioMensaje = 0
                    var mensajesEnviados = 0
                    val FILENAME_FORMATt = "yyyy-MM dd-HH-mm"
                    conteoRabacion++
                    Log.d("sifuniono", grabando.toString())


                    if (grabando!!){

                        Log.d("sifuniono", conteoRabacion.toString())

                        if (conteoRabacion == 2) {
                            val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.iphonenotificacion)
                            mediaPlayer.start()
                            recorder.stop()
                            val audioEntity = AudioEntity(
                                null,
                                archivo.toString(),
                                1
                            )

                            val dataSources =
                                DataSources(AppDatabase.getDatabase(MainActivity.context!!)!!)
                            val repository = RepositoryImpl(dataSources)

                            var job = Job()
                            var uiScope = CoroutineScope(job + Dispatchers.IO)


                            uiScope.launch {
                                repository.guardarAudio(audioEntity)
                            }

                            uiScope.launch {

                                var usuarioLogueado = repository.obtenerUsuarioLogueado()
                                val obtenerDirectorioSqlite = repository.obtenerContactosConfianzaSqlite()

                                obtenerDirectorioSqlite.collect {

                                    if (mensajesEnviados == 0) {
                                        if (!it.isNullOrEmpty()) {
                                            val sms = SmsManager.getDefault()

                                            var mensajeApi: String? = null
                                            var creado: String? = null
                                            var direccionEnviado: String? = null
                                            var ciudad: String? = null
                                            for (i in it) {
                                                Log.d("numero", MainFragment.contactosEnviarMensaje.toString())
                                                //Log.d("numero", usuarioLogueado.toString())
                                                val mensaje =
                                                    "${MainFragment.usuarioLogueado!!.name} puede estar en peligro, llama al ${MainFragment.usuarioLogueado!!.phone_number}https://www.google.com/maps/search/?api=1&query=${MainFragment.ultimaLocalizacion!!.latitude},${MainFragment.ultimaLocalizacion!!.longitude}"

                                                //
                                                sms.sendTextMessage(
                                                    i.number_phone,
                                                    null,
                                                    mensaje,
                                                    null,
                                                    null
                                                )

                                                try {
                                                    var direccion: List<Address> =
                                                        MainFragment.geocoder!!.getFromLocation(MainFragment.ultimaLocalizacion!!.latitude, MainFragment.ultimaLocalizacion!!.longitude, 1)

                                                    Log.d("localizacionn", direccion.toString())

                                                    ciudad =
                                                        "Se envio desde ${direccion[0].locality + "," + direccion[0].adminArea + "," + direccion[0].countryName}"
                                                    direccionEnviado = direccion[0].getAddressLine(0)
                                                    Log.d("reportecreado", ciudad.toString())
                                                    Log.d("reportecreado", direccionEnviado.toString())

                                                    creado =
                                                        SimpleDateFormat(FILENAME_FORMATt, Locale.US).format(
                                                            System.currentTimeMillis()
                                                        )

                                                    //Log.d("createat", creado)

                                                    mensajeApi =
                                                        "https://www.google.com/maps/search/?api=1&query=${MainFragment.ultimaLocalizacion!!.latitude},${MainFragment.ultimaLocalizacion!!.longitude}"

                                                } catch (e: Exception) {
                                                    Log.d("localizacionn", e.toString())
                                                }
                                            }

                                            if (envioMensaje == 0) {
                                                val reporte = ReportesEntity(
                                                    1,
                                                    0,
                                                    mensajeApi,
                                                    MainFragment.ultimaLocalizacion!!.longitude.toString(),
                                                    MainFragment.ultimaLocalizacion!!.latitude.toString(),
                                                    usuarioLogueado.id,
                                                    creado,
                                                    creado!!,
                                                    direccionEnviado.toString(),
                                                    ciudad
                                                )
                                                miLocalizacionLitener.toastDesplegado.postValue(1)
                                                repository.agredarReporte(reporte)
                                            }
                                            envioMensaje = 1
                                        }
                                        mensajesEnviados = 1
                                    }
                                }
                            }
                            conteoRabacion = 0
                        }
                    }else{
                            conteoEnvioMensaje = 2

                    }
                }
            }
        }
        mediaSession!!.setPlaybackToRemote(myVolumeProvider)
        mediaSession!!.isActive = true
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession!!.release()
    }

    fun grabarAudio() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        val path = File(
            context.externalMediaDirs.first(),
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

    fun installListeneIntenetConnectionr(context: Context) {
        if (broadcastReceiver == null) {
            broadcastReceiver = object : BroadcastReceiver() {

                override fun onReceive(context: Context, intent: Intent) {
                    val extras = intent.extras
                    val info = extras?.getParcelable<Parcelable>("networkInfo") as NetworkInfo?
                    val state: NetworkInfo.State = info!!.state
                    Log.d(
                        "InternalBroadcastRecei", info.toString() + " "
                                + state.toString()
                    )
                    if (state === NetworkInfo.State.CONNECTED) {

                    } else {

                    }
                }
            }
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    @SuppressLint("MissingPermission")
    fun registarLocalizacion() {
        Log.d("sifuniono", "estoy enregistrar")

        ubicacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ubicacion!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d("localizacionn", "Red")

            ubicacion!!.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER,
                0,
                0.0F,
                miLocalizacionLitener()
            )
        } else {
            Log.d("localizacionn", "Gps")

            ubicacion!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                600000,
                0.0F,
                miLocalizacionLitener()
            )
        }

    }

    private class miLocalizacionLitener : LocationListener {

        var envioMensaje = 0
        var mensajesEnviados = 0
        private val FILENAME_FORMATt = "yyyy-MM dd-HH-mm"

        companion object {
            val toastDesplegado = MutableLiveData<Int>()
        }

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
            Log.d("me ejecuto", MainFragment.contactosEnviarMensaje.size.toString())

            val dataSources = DataSources(AppDatabase.getDatabase(MainActivity.context!!)!!)
            val repository = RepositoryImpl(dataSources)

            var job = Job()
            var uiScope = CoroutineScope(job + Dispatchers.IO)


            uiScope.launch {
                Log.d("me ejecuto", MainFragment.contactosEnviarMensaje.size.toString())

                Log.d("direccioncambio", location.toString())
                var lat = location.latitude
                var long = location.longitude
                Log.d("ubicacion", "$lat $long")


            }
        }
    }
}