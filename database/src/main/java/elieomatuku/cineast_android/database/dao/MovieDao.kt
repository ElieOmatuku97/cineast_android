package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.database.entity.MovieEntity


/**
 * Created by elieomatuku on 2019-12-06
 */

@Dao
interface MovieDao  {
    @Query("SELECT * from ${MovieEntity.MOVIE_TABLE}")
    suspend fun getAllMovies(): List<MovieEntity>


    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)
}

