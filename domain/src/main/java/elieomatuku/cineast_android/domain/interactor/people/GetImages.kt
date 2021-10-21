package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Images
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.domain.repository.PersonRepository

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetImages(private val personRepository: PersonRepository) :
    UseCase<GetImages.Input, CompleteResult<Images>> {

    override suspend fun execute(params: Input): CompleteResult<Images> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.getImages(
                params.person
            )
        }
    }

    data class Input(val person: Person)
}
