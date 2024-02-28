package elieomatuku.cineast_android.domain.model

data class Discover(
    val content: DiscoverContents = DiscoverContents(),
    val isLoggedIn: Boolean = false
)