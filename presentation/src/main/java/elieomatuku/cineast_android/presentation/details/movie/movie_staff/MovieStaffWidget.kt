package elieomatuku.cineast_android.presentation.details.movie.movie_staff

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import elieomatuku.cineast_android.presentation.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.presentation.widgets.PeopleWidget

@Composable
fun MovieStaffWidget(
    cast: List<Person>,
    crew: List<Person>,
    onItemClick: (content: Content) -> Unit,
    onSeeAllClick: (contents: List<Content>) -> Unit
) {
    Column {
        PeopleWidget(
            people = cast,
            sectionTitle = stringResource(R.string.cast),
            onItemClick = { onItemClick(it) },
            onSeeAllClick = onSeeAllClick
        )

        PeopleWidget(
            people = crew,
            sectionTitle = stringResource(R.string.crew),
            onItemClick = { onItemClick(it) },
            onSeeAllClick = onSeeAllClick
        )
    }
}
