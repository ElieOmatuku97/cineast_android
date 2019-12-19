package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.database.entity.MovieTypeEntity


/**
 * Created by elieomatuku on 2019-12-08
 */

@Dao
interface MovieTypeDao  {
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(types: List<MovieTypeEntity>)

    @Query("SELECT * from ${MovieTypeEntity.MOVIE_TYPE_TABLE}")
    suspend fun getAllTypes(): List<MovieTypeEntity>

    @WorkerThread
    @Query("DELETE FROM ${MovieTypeEntity.MOVIE_TYPE_TABLE} WHERE id = :id")
    suspend fun delete(id: String)

    @WorkerThread
    @Query("DELETE FROM ${MovieTypeEntity.MOVIE_TYPE_TABLE}")
    suspend fun deleteAll()
}
