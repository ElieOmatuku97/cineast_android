package elieomatuku.cineast_android.business.business.model.data


import kotlinx.android.parcel.Parcelize


@Parcelize
data class Cast (val cast_id: Int?, val character: String?,
                 val credit_id: String?, val gender: Int?,
                 override  val id: Int?, override val name: String?,
                 val order: Int?, override val profile_path: String?):  Person()