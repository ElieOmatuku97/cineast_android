package elieomatuku.cineast_android.cache.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.cache.entity.CacheMovie
import elieomatuku.cineast_android.cache.entity.CacheMovieTypeJoin
import elieomatuku.cineast_android.cache.entity.MovieTypeEntity

/**
 * Created by elieomatuku on 2019-12-08
 */

@Dao
interface MovieTypeJoinDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cacheMovieTypeJoin: CacheMovieTypeJoin)

    @Query("SELECT * FROM ${CacheMovie.MOVIE_TABLE} INNER JOIN ${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE} ON ${CacheMovie.MOVIE_TABLE}.id=${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${CacheMovieTypeJoin.MOVIE_ID} WHERE ${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${CacheMovieTypeJoin.MOVIE_TYPE_ID}=:typeId")
    fun getMoviesForType(typeId: String): List<CacheMovie>

    @Query("SELECT * FROM ${MovieTypeEntity.MOVIE_TYPE_TABLE} INNER JOIN ${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE} ON ${MovieTypeEntity.MOVIE_TYPE_TABLE}.id=${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${CacheMovieTypeJoin.MOVIE_TYPE_ID} WHERE  ${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${CacheMovieTypeJoin.MOVIE_ID}=:movieId")
    fun getTypesForMovie(movieId: Int): List<MovieTypeEntity>

    @Query("SELECT * FROM ${CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}")
    fun getMovieTypeJoins(): List<CacheMovieTypeJoin>
}
