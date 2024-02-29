package elieomatuku.cineast_android.presentation.extensions


/**
 * Created by elieomatuku on 2022-07-20
 */

//from https://kotlinlang.org/docs/typecasts.html#type-erasure-and-generic-type-checks
inline fun <reified T> List<*>.asListOfType(): List<T>? =
    if (all { it is T })
        @Suppress("UNCHECKED_CAST")
        this as List<T> else
        null