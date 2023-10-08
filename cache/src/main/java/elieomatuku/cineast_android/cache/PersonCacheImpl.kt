package elieomatuku.cineast_android.cache

import elieomatuku.cineast_android.cache.dao.PersonDao
import elieomatuku.cineast_android.cache.entity.CachePerson
import elieomatuku.cineast_android.data.PrefManager
import elieomatuku.cineast_android.data.model.PersonEntity
import elieomatuku.cineast_android.data.repository.person.PersonCache
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-09-01
 */

class PersonCacheImpl @Inject constructor (private val personDao: PersonDao, private val prefManager: PrefManager) :
    PersonCache {

    private val timeStamp: Long
        get() {
            return prefManager.get(ContentExpiryUtils.TIMESTAMP, null)?.toLong() ?: 0
        }

    override fun updatePerson(person: PersonEntity) {
        personDao.updatePerson(CachePerson.fromPersonEntity(person))
    }

    override fun insertPeople(people: List<PersonEntity>) {
        people.forEach { person ->
            personDao.insertPerson(CachePerson.fromPersonEntity(person))
        }
    }

    override fun insertPerson(person: PersonEntity) {
        personDao.insertPerson(CachePerson.fromPersonEntity(person))
    }

    override fun deletePerson(person: PersonEntity) {
        personDao.delete(person.id)
    }

    override fun deleteAllPeople() {
        personDao.deleteAll()
    }

    override fun getPopularPeople(): List<PersonEntity> {
        return CachePerson.toPeople(personDao.getPopularPeople())
    }

    override fun isCached(): Boolean {
        return !getPopularPeople().isNullOrEmpty()
    }

    override fun isExpired(): Boolean {
        return !ContentExpiryUtils.isUpToDate(timeStamp)
    }
}
