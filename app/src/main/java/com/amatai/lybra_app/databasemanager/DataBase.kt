package com.amatai.lybra_app.databasemanager

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amatai.lybra_app.databasemanager.entities.*


@Database(entities = [UsuarioLogueado::class, ContactosEntity::class, SessionLogueo::class,VideoEntity::class,ReportesEntity::class, Configuracion::class, AudioEntity::class], version = 1)
abstract class AppDatabase:RoomDatabase(){

    abstract fun appDao():AppDao

    companion object{
        private const val DATABASE_NAME = "appdatabase"

        @Volatile
        private  var INSTANCE:AppDatabase? = null

        fun getDatabase(context: Context):AppDatabase?{
            INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance(){
            Log.d("meejecuto", "me ejecuto")
            INSTANCE = null
        }
    }

}