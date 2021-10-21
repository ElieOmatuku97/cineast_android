package elieomatuku.cineast_android.cache.entity

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Created by elieomatuku on 2019-12-08
 */

@Entity(
    tableName = CacheMovieTypeJoin.MOVIE_TYPE_JOIN_TABLE,
    primaryKeys = [CacheMovieTypeJoin.MOVIE_ID, CacheMovieTypeJoin.MOVIE_TYPE_ID],
    foreignKeys = [
        ForeignKey(entity = CacheMovie::class, parentColumns = [CacheMovieTypeJoin.ID], childColumns = [CacheMovieTypeJoin.MOVIE_ID], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = MovieTypeEntity::class, parentColumns = [CacheMovieTypeJoin.ID], childColumns = [CacheMovieTypeJoin.MOVIE_TYPE_ID], onDelete = ForeignKey.CASCADE)
    ]
)
data class CacheMovieTypeJoin(val movieId: Int, val movieTypeId: String) {
    companion object {
        const val MOVIE_TYPE_JOIN_TABLE = "movie_type_join_table"
        const val MOVIE_ID = "movieId"
        const val MOVIE_TYPE_ID = "movieTypeId"
        const val ID = "id"
    }
}
