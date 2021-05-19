package elieomatuku.cineast_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import elieomatuku.cineast_android.database.dao.GenreDao
import elieomatuku.cineast_android.database.dao.MovieDao
import elieomatuku.cineast_android.database.dao.MovieTypeDao
import elieomatuku.cineast_android.database.dao.MovieTypeJoinDao
import elieomatuku.cineast_android.database.dao.PersonalityDao
import elieomatuku.cineast_android.database.entity.GenreEntity
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import elieomatuku.cineast_android.database.entity.MovieTypeJoin
import elieomatuku.cineast_android.database.entity.PersonalityEntity
import java.util.concurrent.Executors

/**
 * Created by elieomatuku on 2019-12-07
 */

@Database(entities = [MovieEntity::class, MovieTypeEntity::class, MovieTypeJoin::class, PersonalityEntity::class, GenreEntity::class], exportSchema = false, version = 1)
@TypeConverters(IntListConverter::class)
abstract class ContentDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieTypeDao(): MovieTypeDao
    abstract fun movieTypeJoinDao(): MovieTypeJoinDao
    abstract fun personalityDao(): PersonalityDao
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

        const val DATABASE_NAME = "content.db"
    }
}
