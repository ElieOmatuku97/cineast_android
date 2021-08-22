package elieomatuku.cineast_android.domain.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


/**
 * Created by elieomatuku on 2021-08-22
 */

inline fun <R> safeUseCaseCall(call: () -> R): CompleteResult<R> {
    return try {
        Success(call())
    } catch (t: Throwable) {
        t.printStackTrace()
        Fail(t)
    }
}

suspend fun <I, T> runUseCase(
    useCase: UseCase<I, T>,
    input: I,
    coroutineContext: CoroutineContext = Dispatchers.IO
): T {
    return withContext(coroutineContext) { useCase.execute(input) }
}
