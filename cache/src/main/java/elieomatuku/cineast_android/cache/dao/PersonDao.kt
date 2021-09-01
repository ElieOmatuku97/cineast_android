package elieomatuku.cineast_android.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import elieomatuku.cineast_android.cache.entity.CachePerson

/**
 * Created by elieomatuku on 2019-12-20
 */

@Dao
interface PersonDao {
    @Query("SELECT * from ${CachePerson.PERSONALITY_TABLE}")
    fun getPopularPeople(): List<CachePerson>

    @Query("SELECT * from ${CachePerson.PERSONALITY_TABLE} WHERE id = :id")
    fun getAllPersonById(id: Int): CachePerson

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(cachePerson: CachePerson)

    @Update
    fun updatePerson(cachePerson: CachePerson)

    @Query("DELETE FROM ${CachePerson.PERSONALITY_TABLE} WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM ${CachePerson.PERSONALITY_TABLE}")
    fun deleteAll()
}
