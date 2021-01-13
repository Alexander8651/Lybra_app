package com.amatai.lybra_app.ui.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media.VolumeProviderCompat
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.ui.activities.MainActivity
import com.amatai.lybra_app.ui.fragments.MainFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import java.util.*

var conteoEnvioMensaje = 6

class PlayerService : Service() {
    private var mediaSession: MediaSessionCompat? = null
    lateinit var context: Context
    var ubicacion: LocationManager? = null

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


        //this will only work on Lollipop and up, see https://code.google.com/p/android/issues/detail?id=224134
        val myVolumeProvider: VolumeProviderCompat = object : VolumeProviderCompat(
            VOLUME_CONTROL_RELATIVE,  /*max volume*/
            100,  /*initial volume level*/
            50
        ) {
            override fun onAdjustVolume(direction: Int) {
                /*
                -1 -- volume down
                1 -- volume up
                0 -- volume button released
                 */

                if (direction == -1) {
                    conteoEnvioMensaje--
                    Log.d("sifuniono", conteoEnvioMensaje.toString())

                    if (conteoEnvioMensaje == 0) {
                        registarLocalizacion()
                        conteoEnvioMensaje = 6
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

    @SuppressLint("MissingPermission")
    fun registarLocalizacion() {
        Log.d("sifuniono", "estoy enregistrar")

        ubicacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        ubicacion!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            50000,
            0.0F,
            miLocalizacionLitener()
        )
    }

    private class miLocalizacionLitener : LocationListener {
        var mensajesEnviados = 0
        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        @InternalCoroutinesApi
        override fun onLocationChanged(location: Location) {
            Log.d("direccioncambio", location.toString())
            var lat = location.latitude
            var long = location.longitude
            Log.d("ubicacion", "$lat $long")

            val dataSources = DataSources(AppDatabase.getDatabase(MainActivity.context!!)!!)
            val repository = RepositoryImpl(dataSources)

            var job = Job()
            var uiScope = CoroutineScope(job + Dispatchers.IO)


            uiScope.launch {

                var usuarioLogueado = repository.obtenerUsuarioLogueado()
                val obtenerDirectorioSqlite = repository.obtenerContactosConfianzaSqlite()

                obtenerDirectorioSqlite.collect {

                    if (mensajesEnviados == 0) {
                        if (!it.isNullOrEmpty()) {
                            val sms = SmsManager.getDefault()
                            for (i in it) {
                                // Log.d("numero", i.numberPhone)
                                Log.d("numero", usuarioLogueado.toString())
                                sms.sendTextMessage(
                                    i.number_phone,
                                    null,
                                    "${usuarioLogueado.name} llamalo al ${usuarioLogueado.phone_number}. https://www.google.com/maps/search/?api=1&query=$lat,$long",
                                    null,
                                    null
                                )
                            }
                        }
                        mensajesEnviados = 1
                    }
                }
            }
        }
    }

}