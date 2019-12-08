package elieomatuku.cineast_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import elieomatuku.cineast_android.database.dao.MovieDao
import elieomatuku.cineast_android.database.dao.MovieTypeDao
import elieomatuku.cineast_android.database.dao.MovieTypeJoinDao
import elieomatuku.cineast_android.database.entity.GenreEntity
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import elieomatuku.cineast_android.database.entity.MovieTypeJoin


/**
 * Created by elieomatuku on 2019-12-07
 */

@Database(entities = [MovieEntity::class, GenreEntity::class, MovieTypeEntity::class, MovieTypeJoin::class], exportSchema = false, version = 1)
@TypeConverters( IntListConverter::class)
abstract class ContentDatabase: RoomDatabase() {

    abstract fun MovieDao(): MovieDao
    abstract fun MovieTypeDao(): MovieTypeDao
    abstract fun MovieTypeJoinDao(): MovieTypeJoinDao

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