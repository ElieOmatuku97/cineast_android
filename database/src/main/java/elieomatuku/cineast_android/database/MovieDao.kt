package elieomatuku.cineast_android.database

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Created by elieomatuku on 2019-12-06
 */

@Dao
interface MovieDao  {
    @Query("SELECT * from ${MovieEntity.MOVIE_TABLE} WHERE type = :type")
    suspend fun getMovieByType(type: String): List<MovieEntity>


    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)
}

