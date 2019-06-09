package elieomatuku.cineast_android.business.business.model.data


import kotlinx.android.parcel.Parcelize



@Parcelize
data class People (override val profile_path: String?, val adult: Boolean?,
                   override val id: Int?,
                   override val name: String? ):Person()
