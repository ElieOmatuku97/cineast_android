package elieomatuku.cineast_android.utils

import androidx.lifecycle.Observer

open class SingleEvent<out T>(private val content: T) {

    var consumed = false
        private set

    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }

    fun peek(): T = content

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleEvent<*>

        if (content != other.content) return false
        if (consumed != other.consumed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + consumed.hashCode()
        return result
    }
}

fun <T> SingleEvent<T>?.consume(block: (T) -> Unit) {
    this?.consume()?.let(block)
}

@Suppress("FunctionName")
fun SingleEvent() = SingleEvent(Unit)

@Suppress("FunctionName")
fun ViewErrorEvent(throwable: Throwable) = SingleEvent(ViewErrorController.mapThrowable(throwable))

class EventObserver<T>(private val onEventUnconsumedContent: (T) -> Unit) :
    Observer<SingleEvent<T>> {
    override fun onChanged(event: SingleEvent<T>?) {
        event?.consume()?.run(onEventUnconsumedContent)
    }
}
