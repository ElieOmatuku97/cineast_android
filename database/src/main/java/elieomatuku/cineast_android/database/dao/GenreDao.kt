package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.database.entity.GenreEntity
import io.reactivex.Maybe


/**
 * Created by elieomatuku on 2019-12-29
 */

@Dao
interface GenreDao {

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genres: List<GenreEntity>)


    @Query("SELECT * from ${GenreEntity.GENRE_TABLE} ORDER BY name")
    fun getAllGenres(): Maybe<List<GenreEntity>>
}