package elieomatuku.cineast_android.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class ViewErrorController(val context: Context) {

    fun showErrorDialog(
        context: Context,
        error: ViewError,
        cancelable: Boolean = true,
        dismissAction: (() -> Unit)? = null
    ) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(error.title)
        builder.setMessage(error.message)
        builder.setPositiveButton("Ok") { _, _ ->
            isShowingError = false
        }
        builder.setOnDismissListener {
            isShowingError = false
            dismissAction?.invoke()
        }
        if (!isShowingError) {
            isShowingError = true
            val dialog = builder.show()
            dialog.setCancelable(cancelable)
            dialog.setCanceledOnTouchOutside(cancelable)
        }
    }

    fun dismissErrorDialog() {
        isShowingError = false
    }

    companion object {
        var isShowingError = false

        fun mapThrowable(
            throwable: Throwable,
            unknownErrorMessage: String = "Unknown"
        ): ViewError {
            Timber.w(throwable)
            return when (throwable) {
//                is RepositoryException -> {
//                    when (throwable.code) {
//                        401 -> {
//                            ViewError(
//                                title = Translation.error.error,
//                                message = Translation.error.authError,
//                                code = -1
//                            )
//                        }
//
//                        403 -> {
//                            ViewError(
//                                title = Translation.error.error,
//                                message = if (throwable.msg.isNotEmpty()) throwable.msg else Translation.error.authError,
//                                code = -1
//                            )
//                        }
//
//                        412 -> {
//                            ViewError(
//                                title = Translation.error.validationError,
//                                message = throwable.msg,
//                                code = -1
//                            )
//                        }
//
//                        429 -> {
//                            ViewError(
//                                title = Translation.error.error,
//                                message = throwable.msg,
//                                code = -1
//                            )
//                        }
//
//                        402, in 404..500 -> {
//                            ViewError(
//                                title = Translation.error.error,
//                                message = unknownErrorMessage,
//                                code = -1
//                            )
//                        }
//                        in 500..600 -> {
//                            ViewError(
//                                title = Translation.error.error,
//                                message = unknownErrorMessage,
//                                code = -1
//                            )
//                        }
//                        else -> {
//                            ViewError(
//                                title = Translation.error.error,
//                                message = unknownErrorMessage,
//                                code = -1
//                            )
//                        }
//                    }
//                }
                else -> {
                    ViewError(
                        title = "Error",
                        message = unknownErrorMessage,
                        code = -1
                    )
                }
            }
        }
    }
}
