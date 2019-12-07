package elieomatuku.cineast_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/**
 * Created by elieomatuku on 2019-12-07
 */

@Database(
        entities = [MovieEntity::class, GenreEntity::class],
        exportSchema = false,
        version = 1)
abstract class ContentDatabase: RoomDatabase() {

    abstract fun MovieDao(): MovieDao


    companion object {

        var INSTANCE: ContentDatabase? = null

        fun getDatabase(context: Context): ContentDatabase {
            if (INSTANCE == null) {
                synchronized(ContentDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context,
                            ContentDatabase::class.java,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }

        const val DATABASE_NAME = "content.db"
    }

}