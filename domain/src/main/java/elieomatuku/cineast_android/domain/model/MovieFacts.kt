package elieomatuku.cineast_android.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieFacts(
    val budget: Int?,
    val releaseDate: String?,
    val runtime: Int?,
    val revenue: Int?,
    val homepage: String?
) : Parcelable {

    val runtimeInHoursAndMinutes: String
        get() {
            val hour: Int? = runtime?.div(60)
            val minutes: Int? = runtime?.rem(60)

            return if (hour != null && minutes != null) {
                val hourString = getHourString(hour)
                val minutesString = getMinutesString(minutes)
                getCombinationOfHourAndMinutes(hourString, minutesString)
            } else {
                "N/A"
            }
        }

    private fun getHourString(hour: Int?): String {
        return when (hour) {
            0 -> ""
            1 -> "$hour hour"
            else -> "$hour hours"
        }
    }

    private fun getMinutesString(minutes: Int?): String {
        return when (minutes) {
            0 -> ""
            1 -> "$minutes minute"
            else -> "$minutes minutes"
        }
    }

    private fun getCombinationOfHourAndMinutes(hours: String, minutes: String): String {
        return if (hours.isNotEmpty() && minutes.isNotEmpty()) {
            "$hours $minutes"
        } else if (hours.isEmpty() && minutes.isNotEmpty()) {
            minutes
        } else if (hours.isNotEmpty() && minutes.isEmpty()) {
            hours
        } else {
            "N/A"
        }
    }
}
