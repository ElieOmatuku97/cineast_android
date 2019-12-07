package elieomatuku.cineast_android.database

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by elieomatuku on 2019-12-06
 */


@Entity(tableName = GenreEntity.GENRE_TABLE)
data class GenreEntity(@PrimaryKey val id: Int, val name: String) {
    companion object {
        const val GENRE_TABLE = "genre_table"
    }
}