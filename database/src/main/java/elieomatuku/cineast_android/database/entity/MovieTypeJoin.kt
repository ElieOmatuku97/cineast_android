package elieomatuku.cineast_android.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Created by elieomatuku on 2019-12-08
 */

@Entity(
    tableName = MovieTypeJoin.MOVIE_TYPE_JOIN_TABLE,
    primaryKeys = [MovieTypeJoin.MOVIE_ID, MovieTypeJoin.MOVIE_TYPE_ID],
    foreignKeys = [
        ForeignKey(entity = MovieEntity::class, parentColumns = [MovieTypeJoin.ID], childColumns = [MovieTypeJoin.MOVIE_ID], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = MovieTypeEntity::class, parentColumns = [MovieTypeJoin.ID], childColumns = [MovieTypeJoin.MOVIE_TYPE_ID], onDelete = ForeignKey.CASCADE)
    ]
)
data class MovieTypeJoin(val movieId: Int, val movieTypeId: String) {
    companion object {
        const val MOVIE_TYPE_JOIN_TABLE = "movie_type_join_table"
        const val MOVIE_ID = "movieId"
        const val MOVIE_TYPE_ID = "movieTypeId"
        const val ID = "id"
    }
}
