package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import elieomatuku.cineast_android.database.entity.PersonalityEntity
import io.reactivex.Flowable

/**
 * Created by elieomatuku on 2019-12-20
 */

@Dao
interface PersonalityDao {
    @Query("SELECT * from ${PersonalityEntity.PERSONALITY_TABLE}")
    fun getAllPersonalities(): Flowable<List<PersonalityEntity>>

    @Query("SELECT * from ${PersonalityEntity.PERSONALITY_TABLE} WHERE id = :id")
    fun getAllPersonalityById(id: Int): Flowable<PersonalityEntity>

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonality(personalityEntity: PersonalityEntity)

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalities(personalityEntities: List<PersonalityEntity>)

    @WorkerThread
    @Update
    fun updatePersonality(personalityEntity: PersonalityEntity)

    @WorkerThread
    @Update
    fun updatePersonalities(PersonalityEntities: List<PersonalityEntity>)

    @WorkerThread
    @Query("DELETE FROM ${PersonalityEntity.PERSONALITY_TABLE} WHERE id = :id")
    fun delete(id: Int)

    @WorkerThread
    @Query("DELETE FROM ${PersonalityEntity.PERSONALITY_TABLE}")
    fun deleteAll()
}
