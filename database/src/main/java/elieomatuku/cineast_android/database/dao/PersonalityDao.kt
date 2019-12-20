package elieomatuku.cineast_android.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieomatuku.cineast_android.database.entity.PersonalityEntity
import io.reactivex.Completable
import io.reactivex.Flowable


/**
 * Created by elieomatuku on 2019-12-20
 */

@Dao
interface PersonalityDao  {
    @Query("SELECT * from ${PersonalityEntity.PERSONALITY_TABLE}")
    fun getAllPersonalities(): Flowable<List<PersonalityEntity>>

    @Query("SELECT * from ${PersonalityEntity.PERSONALITY_TABLE} WHERE id = :id")
    fun getAllPersonalityById(id: Int): Flowable<PersonalityEntity>

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonality(personalityEntity: PersonalityEntity)


    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalities(personalityEntities: List<PersonalityEntity>)
}