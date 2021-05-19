package elieomatuku.cineast_android.core.model

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Crew(
    val credit_id: String,
    val department: String?,
    val gender: Int?,
    override val id: Int?,
    val job: String?,
    override val name: String?,
    override val profile_path: String?
) : Person()
