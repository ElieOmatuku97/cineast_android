package elieomatuku.cineast_android.cache.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import elieomatuku.cineast_android.cache.entity.CacheMovie
import io.reactivex.Flowable

/**
 * Created by elieomatuku on 2019-12-06
 */

@Dao
interface MovieDao {
    @Query("SELECT * from ${CacheMovie.MOVIE_TABLE}")
    fun getAllMovies(): Flowable<List<CacheMovie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(cacheMovie: CacheMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(cacheMovie: List<CacheMovie>)

    @Update
    fun updateMovie(cacheMovie: CacheMovie)

    @Query("DELETE FROM ${CacheMovie.MOVIE_TABLE} WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM ${CacheMovie.MOVIE_TABLE}")
    fun deleteAll()
}
