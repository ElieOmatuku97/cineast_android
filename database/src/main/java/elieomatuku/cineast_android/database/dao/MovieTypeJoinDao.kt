package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.database.entity.MovieTypeEntity
import elieomatuku.cineast_android.database.entity.MovieTypeJoin
import io.reactivex.Flowable

/**
 * Created by elieomatuku on 2019-12-08
 */

@Dao
interface MovieTypeJoinDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieTypeJoin: MovieTypeJoin)

    @Query("SELECT * FROM ${MovieEntity.MOVIE_TABLE} INNER JOIN ${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE} ON ${MovieEntity.MOVIE_TABLE}.id=${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${MovieTypeJoin.MOVIE_ID} WHERE ${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${MovieTypeJoin.MOVIE_TYPE_ID}=:typeId")
    fun getMoviesForType(typeId: String): Flowable<List<MovieEntity>>

    @Query("SELECT * FROM ${MovieTypeEntity.MOVIE_TYPE_TABLE} INNER JOIN ${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE} ON ${MovieTypeEntity.MOVIE_TYPE_TABLE}.id=${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${MovieTypeJoin.MOVIE_TYPE_ID} WHERE  ${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}.${MovieTypeJoin.MOVIE_ID}=:movieId")
    fun getTypesForMovie(movieId: Int): List<MovieTypeEntity>

    @Query("SELECT * FROM ${MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE}")
    fun getMovieTypeJoins(): List<MovieTypeJoin>
}
