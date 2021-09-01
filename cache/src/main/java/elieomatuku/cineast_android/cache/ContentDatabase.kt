package elieomatuku.cineast_android.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import elieomatuku.cineast_android.cache.dao.GenreDao
import elieomatuku.cineast_android.cache.dao.MovieDao
import elieomatuku.cineast_android.cache.dao.MovieTypeDao
import elieomatuku.cineast_android.cache.dao.MovieTypeJoinDao
import elieomatuku.cineast_android.cache.dao.PersonDao
import elieomatuku.cineast_android.cache.entity.CacheGenre
import elieomatuku.cineast_android.cache.entity.CacheMovie
import elieomatuku.cineast_android.cache.entity.CacheMovieTypeJoin
import elieomatuku.cineast_android.cache.entity.CachePerson
import elieomatuku.cineast_android.cache.entity.MovieTypeEntity
import java.util.concurrent.Executors

/**
 * Created by elieomatuku on 2019-12-07
 */

@Database(entities = [CacheMovie::class, MovieTypeEntity::class, CacheMovieTypeJoin::class, CachePerson::class, CacheGenre::class], exportSchema = false, version = 1)
@TypeConverters(IntListConverter::class)
abstract class ContentDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieTypeDao(): MovieTypeDao
    abstract fun movieTypeJoinDao(): MovieTypeJoinDao
    abstract fun personalityDao(): PersonDao
    abstract fun genreDao(): GenreDao

    companion object {
        @Volatile private var INSTANCE: ContentDatabase? = null

        fun getInstance(context: Context): ContentDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): ContentDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ContentDatabase::class.java, DATABASE_NAME
            )
                .addCallback(object : Callback() {

                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        Executors.newSingleThreadScheduledExecutor().execute {
                            getInstance(context).movieTypeDao()
                                .insert(MovieTypeEntity.getPredefinedTypes())
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }

        private const val DATABASE_NAME = "content.db"
    }
}
