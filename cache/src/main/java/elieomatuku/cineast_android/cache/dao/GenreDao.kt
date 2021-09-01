package elieomatuku.cineast_android.cache.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.cache.entity.CacheGenre

/**
 * Created by elieomatuku on 2019-12-29
 */

@Dao
interface GenreDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(cacheGenres: List<CacheGenre>)

    @Query("SELECT * from ${CacheGenre.GENRE_TABLE} ORDER BY name")
    fun getAllGenres(): List<CacheGenre>
}
