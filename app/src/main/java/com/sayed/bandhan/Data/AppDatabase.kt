package com.sayed.bandhan.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Main database class for the app that holds the database and serves as the main access point.
 *
 * @param entities The list of entity classes (tables) in the database.
 * @param version The version number of the database.
 * @param exportSchema Whether to export the schema into a folder.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // The DAO for the User entity
    abstract fun userDao(): UserDao

    companion object {
        // Singleton pattern to ensure only one instance of the database exists
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bandhan_database" // The name of the database file
                )
                    .fallbackToDestructiveMigration() // Allows database migration without handling
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
