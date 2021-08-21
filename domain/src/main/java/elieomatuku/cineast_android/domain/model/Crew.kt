package elieomatuku.cineast_android.domain.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Crew(
    val creditId: String,
    val department: String?,
    val gender: Int?,
    override val id: Int?,
    val job: String?,
    override val name: String?,
    override val profilePath: String?
) : Person()
