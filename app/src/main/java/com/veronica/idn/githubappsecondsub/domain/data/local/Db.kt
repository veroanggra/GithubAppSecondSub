package com.veronica.idn.githubappsecondsub.domain.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.veronica.idn.githubappsecondsub.domain.data.model.DetailUserResponse

@Database(entities = [DetailUserResponse::class], version = 1)
abstract class Db : RoomDatabase() {
    abstract fun favDao() : FavoriteDao
    companion object{
        @Volatile
        var instance : Db? = null

        fun dataBase(context: Context) : Db{
            if (instance == null){
                synchronized(Db::class.java){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Db::class.java, "favorite"
                    ).build()
                }
            }
            return instance as Db
        }
    }
}