package com.example.gamesmanagerapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamesmanagerapp.dao.PlayerDao
import com.example.gamesmanagerapp.dao.TeamDao
import com.example.gamesmanagerapp.entities.Player
import com.example.gamesmanagerapp.entities.Team
import com.example.gamesmanagerapp.views.PlayerDetail

@Database(entities = [Player::class, Team::class], views = [PlayerDetail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "games-manager.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}