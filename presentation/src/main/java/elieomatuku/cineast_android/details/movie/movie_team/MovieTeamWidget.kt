package elieomatuku.cineast_android.details.movie.movie_team

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.widgets.PeopleWidget

@Composable
fun MovieTeamWidget(
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
