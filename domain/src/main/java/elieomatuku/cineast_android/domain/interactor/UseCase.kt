package elieomatuku.cineast_android.domain.interactor


/**
 * Created by elieomatuku on 2021-08-22
 */

interface UseCase<In, Out> {
    suspend fun execute(params: In): Out
}

typealias NoInputUseCase<Out> = UseCase<Unit, Out>

typealias NoOutputUseCase<In> = UseCase<In, Unit>