package elieomatuku.cineast_android.domain.interactor.people

import elieomatuku.cineast_android.domain.interactor.CompleteResult
import elieomatuku.cineast_android.domain.interactor.UseCase
import elieomatuku.cineast_android.domain.interactor.safeUseCaseCall
import elieomatuku.cineast_android.domain.model.Images
import elieomatuku.cineast_android.domain.repository.PersonRepository
import javax.inject.Inject

/**
 * Created by elieomatuku on 2021-08-22
 */

class GetImages @Inject constructor(private val personRepository: PersonRepository) :
    UseCase<GetImages.Input, CompleteResult<Images>> {

    override suspend fun execute(params: Input): CompleteResult<Images> {
        return safeUseCaseCall {
            return@safeUseCaseCall personRepository.getImages(
                params.personId
            )
        }
    }

    data class Input(val personId: Int)
}
