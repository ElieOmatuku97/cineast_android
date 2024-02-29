package elieomatuku.cineast_android.presentation.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import elieomatuku.cineast_android.R
import elieomatuku.cineast_android.domain.model.Content
import elieomatuku.cineast_android.domain.model.Person
import elieomatuku.cineast_android.presentation.utils.UiUtils

/**
 * Created by elieomatuku on 2022-07-20
 */

@Composable
fun PeopleWidget(
    people: List<Person>,
    sectionTitle: String,
    onItemClick: (content: Content) -> Unit,
    onSeeAllClick: (contents: List<Content>) -> Unit
) {
    Surface {
        Column(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_medium),
                        end = dimensionResource(id = R.dimen.padding_small),
                        top = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_xsmall)
                    )
                    .clickable(onClick = { onSeeAllClick(people) })
            ) {
                Text(
                    text = sectionTitle
                )
                Row(horizontalArrangement = Arrangement.End) {
                    Text(
                        stringResource(id = R.string.see_all),
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_keyboard_arrow_right_black_24dp),
                        contentDescription = null,
                    )
                }
            }
            LazyRow(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small)
                )
            ) {
                items(people) { person ->
                    PeopleItem(person = person, onPersonClick = onItemClick)
                }
            }
        }
    }
}

@Composable
fun PeopleItem(
    person: Person,
    onPersonClick: (content: Content) -> Unit
) {
    val imageUrl =
        UiUtils.getImageUrl(person.imagePath, stringResource(id = R.string.image_small))
    Column(Modifier.clickable(onClick = { onPersonClick.invoke(person) })) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
            ),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.image_height_xxlarge))
                .width(dimensionResource(id = R.dimen.image_width_xlarge))
        )
        person.name?.let {
            Text(
                text = it,
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.text_size_small).value.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small),
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small)
                    )
                    .widthIn(max = 70.dp)
            )
        }
    }
}